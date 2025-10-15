package com.books.services.books.admins;


import com.books.services.embeddings.IVectorStoreService;
import com.books.utils.converters.BooksConverter;
import com.books.dto.BookSingleItemDto;
import com.books.dto.CreateBookDto;
import com.books.entities.Book;
import com.books.repositories.BooksRepository;
import com.books.services.storage.ICloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
public class AdminsBooksService implements IAdminsBooksService {

    private final BooksRepository booksRepository;
    private final ICloudService gcsService;
    private final IVectorStoreService<Book> vectorService;

    @Autowired
    public AdminsBooksService(BooksRepository booksRepository, ICloudService gcsService, IVectorStoreService<Book> vectorService) {
        this.booksRepository = booksRepository;
        this.gcsService = gcsService;
        this.vectorService = vectorService;
    }

    @Override
    public BookSingleItemDto createBook(CreateBookDto createBookDto, MultipartFile file) throws IOException {
        String fileUrl = gcsService.uploadFile(file, UUID.randomUUID() + "-" + file.getOriginalFilename());
        createBookDto.setCoverLink(fileUrl);
        Book book = BooksConverter.convertCreateBookDtoToBook(createBookDto);
        Book savedBook = booksRepository.save(book);
        vectorService.addToVectorStore(savedBook);
        return BooksConverter.convertBookToBookSingleItemDto(savedBook);
    }

    @Override
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

        return BooksConverter.convertBookToBookSingleItemDto(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id " + id));

        gcsService.deleteFile(book.getCoverLink());
        vectorService.deleteFromVectorStore(book.getId());
        booksRepository.delete(book);
    }

}
