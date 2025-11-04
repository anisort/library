package com.books.services;

import com.books.dto.BookItemListDto;
import com.books.dto.BookSingleItemDto;
import com.books.dto.PagedResponseDto;
import com.books.entities.Book;
import com.books.repositories.BooksRepository;
import com.books.repositories.UserBooksRepository;
import com.books.repositories.projections.TopBookProjection;
import com.books.services.books.all.PublicBooksServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.books.dto.BookItemDto;
import com.books.utils.converters.BooksConverter;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PublicBooksServiceTest {

    @Mock
    BooksRepository booksRepository;

    @Mock
    UserBooksRepository userBooksRepository;

    @InjectMocks
    PublicBooksServiceImpl publicBooksService;

    @Test
    void getAllBooksWithoutLetter() {
        Book book = new Book(1L, "Title", "Author", "Summary", "Link", "Cover link");

        Page<Book> page = new PageImpl<>(List.of(book));
        when(booksRepository.findAll(any(Pageable.class))).thenReturn(page);

        BookItemDto bookItemDto = BooksConverter.convertBookToBookItemDto(book);

        try (MockedStatic<BooksConverter> st = mockStatic(BooksConverter.class)) {
            st.when(() -> BooksConverter.convertBookToBookItemDto(book)).thenReturn(bookItemDto);

            PagedResponseDto<BookItemDto> result = publicBooksService.getAllBooks(PageRequest.of(0, 10), null);

            assertEquals(1, result.getTotalElements());
            assertEquals("Title", result.getContent().getFirst().getTitle());
            assertEquals("Author", result.getContent().getFirst().getAuthor());
            assertEquals("Cover link", result.getContent().getFirst().getCoverLink());

            verify(booksRepository).findAll(any(Pageable.class));
        }
    }

    @Test
    void getAllBooksWithLetter() {

        Book bookA = new Book(1L,"Alpha","Author","Summary","Link","Cover link");

        Page<Book> page = new PageImpl<>(List.of(bookA));
        when(booksRepository.findByTitleStartingWithIgnoreCase(eq("A"), any(Pageable.class))).thenReturn(page);

        BookItemDto bookItemDtoA = BooksConverter.convertBookToBookItemDto(bookA);

        try (var st = mockStatic(BooksConverter.class)) {
            st.when(() -> BooksConverter.convertBookToBookItemDto(bookA)).thenReturn(bookItemDtoA);

            PagedResponseDto<BookItemDto> result = publicBooksService.getAllBooks(PageRequest.of(0,5), "A");

            assertEquals(1, result.getTotalElements());
            assertEquals("Alpha", result.getContent().getFirst().getTitle());
            verify(booksRepository).findByTitleStartingWithIgnoreCase(eq("A"), any(Pageable.class));
        }
    }

    @Test
    void getTopBooks() {

        Book book = new Book(1L, "Top Book", "Top Author", "Summary", "Link", "Cover link");
        TopBookProjection projection = () -> book;
        when(userBooksRepository.findTopBooks(PageRequest.of(0, 3))).thenReturn(List.of(projection));
        BookItemDto bookItemDto = BooksConverter.convertBookToBookItemDto(book);

        try (MockedStatic<BooksConverter> st = mockStatic(BooksConverter.class)) {
            st.when(() -> BooksConverter.convertBookToBookItemDto(book)).thenReturn(bookItemDto);

            BookItemListDto result = publicBooksService.getTopBooks(3);

            assertEquals(1, result.getBooks().size());
            assertEquals("Top Book", result.getBooks().getFirst().getTitle());
            assertEquals("Top Author", result.getBooks().getFirst().getAuthor());
            assertEquals("Cover link", result.getBooks().getFirst().getCoverLink());

            verify(userBooksRepository).findTopBooks(PageRequest.of(0, 3));
        }
    }

    @Test
    void searchBooksByTitle() {
        when(booksRepository.findTop10ByTitleContainingIgnoreCase("test_title")).thenReturn(List.of());
        publicBooksService.searchBooks("title", "test_title");
        verify(booksRepository).findTop10ByTitleContainingIgnoreCase("test_title");
    }

    @Test
    void searchBooksByAuthor() {
        when(booksRepository.findTop10ByAuthorContainingIgnoreCase("test_author")).thenReturn(List.of());
        publicBooksService.searchBooks("author", "test_author");
        verify(booksRepository).findTop10ByAuthorContainingIgnoreCase("test_author");
    }

    @Test
    void getBookByIdSuccessfully() {
        Book book = new Book(1L,"Title","Author","Summary","Link","CoverLink");
        when(booksRepository.findById(1L)).thenReturn(Optional.of(book));

        BookSingleItemDto bookSingleItemDto = BooksConverter.convertBookToBookSingleItemDto(book);

        try (var st = mockStatic(BooksConverter.class)) {
            st.when(() -> BooksConverter.convertBookToBookSingleItemDto(book)).thenReturn(bookSingleItemDto);

            BookSingleItemDto result = publicBooksService.getBookById(1L);

            assertEquals(1L, result.getId());
            verify(booksRepository).findById(1L);
        }
    }

    @Test
    void getBookByIdFailure() {
        when(booksRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> publicBooksService.getBookById(404L));
        verify(booksRepository).findById(404L);
    }
}
