package com.books.services;

import com.books.converters.BooksConverter;
import com.books.dto.MyBookItemDto;
import com.books.dto.MyBookSingleItemDto;
import com.books.entities.Book;
import com.books.entities.UserBook;
import com.books.repositories.BooksRepository;
import com.books.repositories.UserBooksRepository;
import com.books.services.books.users.UsersBooksService;
import com.books.utils.enums.BookStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersBooksServiceTest {

    @Mock
    BooksRepository booksRepository;

    @Mock
    UserBooksRepository userBooksRepository;

    @InjectMocks
    UsersBooksService userBooksService;

    @Test
    void getUserBooksWithoutStatus() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 5);

        Book book = new Book(10L, "Title", "Author", "Summary", "Link", "Cover");
        UserBook userBook = new UserBook(userId, book, BookStatus.READING);

        Page<UserBook> page = new PageImpl<>(List.of(userBook), pageable, 1);

        when(userBooksRepository.findAllByUserId(userId, pageable)).thenReturn(page);

        try (MockedStatic<BooksConverter> converterMock = mockStatic(BooksConverter.class)) {
            MyBookItemDto myBookItemDto = new MyBookItemDto();
            converterMock.when(() -> BooksConverter.convertBookToMyBookItemDto(book, BookStatus.READING)).thenReturn(myBookItemDto);

            Page<MyBookItemDto> result = userBooksService.getUserBooks(userId, pageable, null);

            assertEquals(1, result.getTotalElements());
            assertSame(myBookItemDto, result.getContent().getFirst());

            verify(userBooksRepository).findAllByUserId(userId, pageable);
        }
    }

    @Test
    void addBookToUserLibrary() {
        Long userId = 1L;
        Long bookId = 10L;
        BookStatus status = BookStatus.TO_READ;

        Book book = new Book(bookId, "Title", "Author", "Summary", "Link", "Cover");
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userBooksRepository.findByUserIdAndBookId(userId, bookId)).thenReturn(Optional.empty());

        try (MockedStatic<BooksConverter> converterMock = mockStatic(BooksConverter.class)) {
            MyBookSingleItemDto myBookSingleItemDto = new MyBookSingleItemDto();
            converterMock.when(() -> BooksConverter.convertBookToMyBookSingleItem(book, status)).thenReturn(myBookSingleItemDto);

            MyBookSingleItemDto result = userBooksService.addBookToUserLibrary(userId, bookId, status);

            assertSame(myBookSingleItemDto, result);

            ArgumentCaptor<UserBook> captor = ArgumentCaptor.forClass(UserBook.class);
            verify(userBooksRepository).save(captor.capture());

            UserBook saved = captor.getValue();
            assertEquals(userId, saved.getUserId());
            assertEquals(book, saved.getBook());
            assertEquals(status, saved.getBookStatus());
        }
    }

    @Test
    void updateBookStatus() {
        Long userId = 1L;
        Long bookId = 10L;
        BookStatus oldStatus = BookStatus.TO_READ;
        BookStatus newStatus = BookStatus.READING;

        Book book = new Book(bookId, "Title", "Author", "Summary", "Link", "Cover");
        UserBook userBook = new UserBook(userId, book, oldStatus);

        when(userBooksRepository.findByUserIdAndBookId(userId, bookId)).thenReturn(Optional.of(userBook));

        try (MockedStatic<BooksConverter> converterMock = mockStatic(BooksConverter.class)) {
            MyBookSingleItemDto myBookSingleItemDto = new MyBookSingleItemDto();
            converterMock.when(() -> BooksConverter.convertBookToMyBookSingleItem(book, newStatus)).thenReturn(myBookSingleItemDto);

            MyBookSingleItemDto result = userBooksService.updateBookStatus(userId, bookId, newStatus);

            assertSame(myBookSingleItemDto, result);
            assertEquals(newStatus, userBook.getBookStatus());

            verify(userBooksRepository).save(userBook);
        }
    }

    @Test
    void removeBookFromUserLibrary() {
        Long userId = 1L;
        Long bookId = 10L;

        Book book = new Book(bookId, "Title", "Author", "Summary", "Link", "Cover");
        UserBook userBook = new UserBook(userId, book, BookStatus.READ);

        when(userBooksRepository.findByUserIdAndBookId(userId, bookId)).thenReturn(Optional.of(userBook));

        userBooksService.removeBookFromUserLibrary(userId, bookId);

        verify(userBooksRepository).delete(userBook);
    }
}
