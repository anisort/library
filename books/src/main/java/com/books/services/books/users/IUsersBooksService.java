package com.books.services.books.users;

import com.books.dto.MyBookItemDto;
import com.books.dto.MyBookSingleItemDto;
import com.books.utils.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUsersBooksService {

    Page<MyBookItemDto> getUserBooks(Long userId, Pageable pageable, BookStatus bookStatus);

    Optional<BookStatus> getBookStatusInUserLibrary(Long userId, Long bookId);

    MyBookSingleItemDto addBookToUserLibrary(Long userId, Long bookId, BookStatus bookStatus);

    MyBookSingleItemDto updateBookStatus(Long userId, Long bookId, BookStatus newStatus);

    void removeBookFromUserLibrary(Long userId, Long bookId);

}
