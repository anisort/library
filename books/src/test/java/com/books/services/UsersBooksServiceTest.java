package com.books.services;

import com.books.converters.BooksConverter;
import com.books.dto.MyBookItemDto;
import com.books.entities.Book;
import com.books.entities.UserBook;
import com.books.repositories.BooksRepository;
import com.books.repositories.UserBooksRepository;
import com.books.services.books.users.UsersBooksService;
import com.books.utils.enums.BookStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
            MyBookItemDto dto = new MyBookItemDto();
            converterMock.when(() -> BooksConverter.convertBookToMyBookItemDto(book, BookStatus.READING)).thenReturn(dto);

            Page<MyBookItemDto> result = userBooksService.getUserBooks(userId, pageable, null);

            assertEquals(1, result.getTotalElements());
            assertSame(dto, result.getContent().getFirst());

            verify(userBooksRepository).findAllByUserId(userId, pageable);
        }
    }

    @Test
    void getUserBooksWithStatus() {

    }

    @Test
    void addBookToUserLibrary() {

    }

    @Test
    void updateBookStatus() {

    }

    @Test
    void removeBookFromUserLibrary() {

    }
}
