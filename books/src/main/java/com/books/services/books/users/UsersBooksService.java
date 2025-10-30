package com.books.services.books.users;

import com.books.dto.PagedResponseDto;
import com.books.utils.converters.BooksConverter;
import com.books.dto.MyBookItemDto;
import com.books.dto.MyBookSingleItemDto;
import com.books.entities.Book;
import com.books.entities.UserBook;
import com.books.repositories.BooksRepository;
import com.books.repositories.UserBooksRepository;
import com.books.utils.converters.PageConverter;
import com.books.utils.enums.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UsersBooksService implements  IUsersBooksService {

    private final BooksRepository booksRepository;
    private final UserBooksRepository userBooksRepository;

    @Autowired
    public UsersBooksService(BooksRepository booksRepository, UserBooksRepository userBooksRepository) {
        this.booksRepository = booksRepository;
        this.userBooksRepository = userBooksRepository;
    }

    @Override
    public PagedResponseDto<MyBookItemDto> getUserBooks(Long userId, Pageable pageable, BookStatus bookStatus) {
        Page<UserBook> userBooks;
        if (bookStatus != null && !bookStatus.toString().isEmpty()) {
            userBooks = userBooksRepository.findAllByUserIdAndBookStatus(userId, bookStatus, pageable);
        } else {
            userBooks = userBooksRepository.findAllByUserId(userId, pageable);
        }
        return PageConverter.convertPageToPagedResponseDto(userBooks.map(userBook -> BooksConverter.convertBookToMyBookItemDto(userBook.getBook(), userBook.getBookStatus())));
    }


    @Override
    public Optional<BookStatus> getBookStatusInUserLibrary(Long userId, Long bookId) {
        return userBooksRepository.findByUserIdAndBookId(userId, bookId)
                .map(UserBook::getBookStatus);
    }

    @Override
    public MyBookSingleItemDto addBookToUserLibrary(Long userId, Long bookId, BookStatus bookStatus) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id " + bookId));

        boolean alreadyAdded = userBooksRepository.findByUserIdAndBookId(userId, bookId).isPresent();
        if (alreadyAdded) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book already in library");
        }

        UserBook userBook = new UserBook(userId, book, bookStatus);
        userBooksRepository.save(userBook);

        return BooksConverter.convertBookToMyBookSingleItem(book, bookStatus);
    }

    @Override
    public MyBookSingleItemDto updateBookStatus(Long userId, Long bookId, BookStatus newStatus) {
        UserBook userBook = userBooksRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found in user's library"));

        userBook.setBookStatus(newStatus);
        userBooksRepository.save(userBook);

        return BooksConverter.convertBookToMyBookSingleItem(userBook.getBook(), newStatus);
    }

    @Override
    public void removeBookFromUserLibrary(Long userId, Long bookId) {
        UserBook userBook = userBooksRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found in user's library"));
        userBooksRepository.delete(userBook);
    }

}
