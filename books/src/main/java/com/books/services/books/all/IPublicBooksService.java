package com.books.services.books.all;

import com.books.dto.BookItemDto;
import com.books.dto.BookItemListDto;
import com.books.dto.BookSingleItemDto;
import com.books.dto.PagedResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPublicBooksService {

    PagedResponseDto<BookItemDto> getAllBooks(Pageable pageable, String letter);

    BookSingleItemDto getBookById(Long id);

    BookItemListDto getTopBooks(int limit);

    List<BookItemDto> searchBooks(String filter, String searchText);
}
