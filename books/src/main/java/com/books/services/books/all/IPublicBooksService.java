package com.books.services.books.all;

import com.books.dto.BookItemDto;
import com.books.dto.BookSingleItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPublicBooksService {

    Page<BookItemDto> getAllBooks(Pageable pageable, String letter);

    BookSingleItemDto getBookById(Long id);

    List<BookItemDto> getTopBooks(int limit);

    List<BookItemDto> searchBooks(String filter, String searchText);
}
