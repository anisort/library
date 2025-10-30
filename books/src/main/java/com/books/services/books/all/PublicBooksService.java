package com.books.services.books.all;

import com.books.dto.BookItemListDto;
import com.books.dto.PagedResponseDto;
import com.books.utils.converters.BooksConverter;
import com.books.dto.BookItemDto;
import com.books.dto.BookSingleItemDto;
import com.books.entities.Book;
import com.books.repositories.BooksRepository;
import com.books.repositories.UserBooksRepository;
import com.books.repositories.projections.TopBookProjection;
import com.books.utils.converters.PageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(
            value = "ALL_BOOKS_CACHE",
            key = "'page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize + "
                    + "'_sort_' + #pageable.sort.toString() + "
                    + "'_letter_' + (#letter != null ? #letter : 'all')"
    )
    public PagedResponseDto<BookItemDto> getAllBooks(Pageable pageable, String letter) {
        Page<Book> books;
        if (letter != null && !letter.isEmpty()) {
            books = booksRepository.findByTitleStartingWithIgnoreCase(letter, pageable);
        } else {
            books = booksRepository.findAll(pageable);
        }
        return PageConverter.convertPageToPagedResponseDto(books.map(BooksConverter::convertBookToBookItemDto));
    }

    @Override
    @Cacheable(value = "TOP_BOOKS_CACHE", key = "'top'")
    public BookItemListDto getTopBooks(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<TopBookProjection> topBooks = userBooksRepository.findTopBooks(pageable);

        List<BookItemDto> bookItems = topBooks.stream()
                .map(proj -> BooksConverter.convertBookToBookItemDto(proj.getBook()))
                .toList();

        BookItemListDto bookItemListDto = new BookItemListDto();
        bookItemListDto.setBooks(bookItems);
        bookItemListDto.setLimit(limit);

        return bookItemListDto;
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
    @Cacheable(value = "BOOK_CACHE", key = "#id")
    public BookSingleItemDto getBookById(Long id) {
        Book book = booksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id " + id));
        return BooksConverter.convertBookToBookSingleItemDto(book);
    }
}
