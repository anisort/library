package com.books.services.books.admins;

import com.books.dto.BookItemDto;
import com.books.services.cache.ICacheUpdateService;
import com.books.services.embeddings.IVectorStoreService;
import com.books.utils.converters.BooksConverter;
import com.books.dto.BookSingleItemDto;
import com.books.dto.CreateBookDto;
import com.books.entities.Book;
import com.books.repositories.BooksRepository;
import com.books.services.storage.ICloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class AdminsBooksService implements IAdminsBooksService {

    private final BooksRepository booksRepository;
    private final ICloudService gcsService;
    private final IVectorStoreService<Book> vectorService;
    private final ICacheUpdateService<BookItemDto> cacheUpdateService;

    @Autowired
    public AdminsBooksService(
            BooksRepository booksRepository,
            ICloudService gcsService,
            IVectorStoreService<Book> vectorService,
            ICacheUpdateService<BookItemDto> cacheUpdateService
    ) {
        this.booksRepository = booksRepository;
        this.gcsService = gcsService;
        this.vectorService = vectorService;
        this.cacheUpdateService = cacheUpdateService;
    }

    @Override
    @CachePut(value = "BOOK_CACHE", key = "#result.getId()")
    @CacheEvict(value = "ALL_BOOKS_CACHE", allEntries = true)
    public BookSingleItemDto createBook(CreateBookDto createBookDto, MultipartFile file) throws IOException {
        String fileUrl = gcsService.uploadFile(file, UUID.randomUUID() + "-" + file.getOriginalFilename());
        createBookDto.setCoverLink(fileUrl);
        Book book = BooksConverter.convertCreateBookDtoToBook(createBookDto);
        Book savedBook = booksRepository.save(book);
        vectorService.addToVectorStore(List.of(savedBook));
        return BooksConverter.convertBookToBookSingleItemDto(savedBook);
    }

    @Override
    @CachePut(value = "BOOK_CACHE", key = "#result.getId()")
    @CacheEvict(value = "ALL_BOOKS_CACHE", allEntries = true)
    public BookSingleItemDto updateBook(Long id, CreateBookDto updateBookDto, MultipartFile newFile) throws IOException {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id " + id));

        if (newFile != null && !newFile.isEmpty()) {
            gcsService.deleteFile(book.getCoverLink());
            String newFileUrl = gcsService.uploadFile(newFile, UUID.randomUUID() + "-" + newFile.getOriginalFilename());
            updateBookDto.setCoverLink(newFileUrl);
        } else {
            updateBookDto.setCoverLink(book.getCoverLink());
        }

        BooksConverter.updateBookEntity(book, updateBookDto);
        book = booksRepository.save(book);

        vectorService.updateInVectorStore(book);

        BookSingleItemDto updatedDto = BooksConverter.convertBookToBookSingleItemDto(book);
        cacheUpdateService.updateIfPresent("TOP_BOOKS_CACHE", "top", BooksConverter.convertBookSingleToBookItemDto(updatedDto));

        return BooksConverter.convertBookToBookSingleItemDto(book);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "BOOK_CACHE", key = "#id"),
            @CacheEvict(value = "ALL_BOOKS_CACHE", allEntries = true)
    })
    public void deleteBook(Long id) {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id " + id));

        gcsService.deleteFile(book.getCoverLink());
        vectorService.deleteFromVectorStore(book.getId());
        booksRepository.delete(book);
        cacheUpdateService.evictIfContains("TOP_BOOKS_CACHE", "top", id);
    }

}
