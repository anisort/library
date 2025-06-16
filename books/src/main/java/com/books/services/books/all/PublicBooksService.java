package com.books.services.books.all;

import com.books.converters.BooksConverter;
import com.books.dto.BookItemDto;
import com.books.dto.BookSingleItemDto;
import com.books.entities.Book;
import com.books.repositories.BooksRepository;
import com.books.repositories.UserBooksRepository;
import com.books.repositories.projections.TopBookProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PublicBooksService implements IPublicBooksService{

    private final BooksRepository booksRepository;
    private final UserBooksRepository userBooksRepository;

    @Autowired
    public PublicBooksService(BooksRepository booksRepository, UserBooksRepository userBooksRepository) {
        this.booksRepository = booksRepository;
        this.userBooksRepository = userBooksRepository;
    }

    @Override
    public Page<BookItemDto> getAllBooks(Pageable pageable, String letter) {
        Page<Book> books;
        if (letter != null && !letter.isEmpty()) {
            books = booksRepository.findByTitleStartingWithIgnoreCase(letter, pageable);
        } else {
            books = booksRepository.findAll(pageable);
        }
        return books.map(BooksConverter::convertBookToBookItemDto);
    }

    @Override
    public List<BookItemDto> getTopBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<TopBookProjection> topBooks = userBooksRepository.findTopBooks(pageable);
        return topBooks.stream()
                .map(proj -> BooksConverter.convertBookToBookItemDto(proj.getBook()))
                .toList();
    }

    @Override
    public List<BookItemDto> searchBooks(String filter, String searchText) {
        List<Book> books;
        switch (filter.toLowerCase()) {
            case "title" -> books = booksRepository.findTop10ByTitleContainingIgnoreCase(searchText);
            case "author" -> books = booksRepository.findTop10ByAuthorContainingIgnoreCase(searchText);
            default -> throw new IllegalArgumentException("Unsupported filter: " + filter);
        }
        return books.stream()
                .map(BooksConverter::convertBookToBookItemDto)
                .toList();
    }

    @Override
    public BookSingleItemDto getBookById(Long id) {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id " + id));
        return BooksConverter.convertBookToBookSingleItemDto(book);
    }
}
