package com.books.services.books.admins;


import com.books.utils.converters.BooksConverter;
import com.books.dto.BookSingleItemDto;
import com.books.dto.CreateBookDto;
import com.books.entities.Book;
import com.books.repositories.BooksRepository;
import com.books.services.storage.ICloudService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminsBooksService implements IAdminsBooksService {

    private final BooksRepository booksRepository;
    private final ICloudService gcsService;
    private final VectorStore vectorStore;

    @Autowired
    public AdminsBooksService(BooksRepository booksRepository, ICloudService gcsService, VectorStore vectorStore) {
        this.booksRepository = booksRepository;
        this.gcsService = gcsService;
        this.vectorStore = vectorStore;
    }

    @Override
    public BookSingleItemDto createBook(CreateBookDto createBookDto, MultipartFile file) throws IOException {
        String fileUrl = gcsService.uploadFile(file, UUID.randomUUID() + "-" + file.getOriginalFilename());
        createBookDto.setCoverLink(fileUrl);
        Book book = BooksConverter.convertCreateBookDtoToBook(createBookDto);
        Book savedBook = booksRepository.save(book);

        UUID uuid = UUID.nameUUIDFromBytes(String.valueOf(book.getId()).getBytes());
        Document document = new Document(uuid.toString(), book.getSummary(), Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "author", book.getAuthor(),
                "coverLink", book.getCoverLink(),
                "link", book.getLink()
        ));
        vectorStore.add(List.of(document));

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

        UUID uuid = UUID.nameUUIDFromBytes(String.valueOf(book.getId()).getBytes());
        vectorStore.delete(List.of(String.valueOf(uuid)));
        Document document = new Document(uuid.toString(), book.getSummary(), Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "author", book.getAuthor(),
                "coverLink", book.getCoverLink(),
                "link", book.getLink()
        ));
        vectorStore.add(List.of(document));

        return BooksConverter.convertBookToBookSingleItemDto(book);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id " + id));

        gcsService.deleteFile(book.getCoverLink());

        UUID uuid = UUID.nameUUIDFromBytes(String.valueOf(book.getId()).getBytes());
        vectorStore.delete(List.of(String.valueOf(uuid)));

        booksRepository.delete(book);
    }

}
