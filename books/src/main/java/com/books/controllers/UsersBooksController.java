package com.books.controllers;

import com.books.dto.AddBookStatusDto;
import com.books.dto.MyBookItemDto;
import com.books.dto.MyBookSingleItemDto;
import com.books.services.books.users.IUsersBooksService;
import com.books.utils.enums.BookStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user/books")
public class UsersBooksController {

    private final IUsersBooksService usersBooksService;

    @Autowired
    public UsersBooksController(IUsersBooksService usersBooksService) {
        this.usersBooksService = usersBooksService;
    }

    @GetMapping
    public Page<MyBookItemDto> getAllBooks(Pageable pageable, @RequestParam(required = false) BookStatus bookStatus, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return usersBooksService.getUserBooks(userId, pageable, bookStatus);
    }

    @GetMapping("/{bookId}")
    public Optional<BookStatus> getBookStatusInUserLibrary(@PathVariable Long bookId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return usersBooksService.getBookStatusInUserLibrary(userId, bookId);
    }

    @PostMapping("{bookId}")
    public MyBookSingleItemDto addBookToUserLibrary(@PathVariable Long bookId, @AuthenticationPrincipal Jwt jwt, @RequestBody @Valid AddBookStatusDto addBookStatusDto) {
        Long userId = Long.valueOf(jwt.getSubject());
        return usersBooksService.addBookToUserLibrary(userId, bookId, addBookStatusDto.getBookStatus());
    }

    @PatchMapping("/{bookId}")
    public MyBookSingleItemDto updateBookStatus(@PathVariable Long bookId, @AuthenticationPrincipal Jwt jwt, @RequestBody @Valid AddBookStatusDto addBookStatusDto) {
        Long userId = Long.valueOf(jwt.getSubject());
        return usersBooksService.updateBookStatus(userId, bookId, addBookStatusDto.getBookStatus());
    }

    @DeleteMapping("{bookId}")
    public void removeBookFromUserLibrary(@PathVariable Long bookId, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        usersBooksService.removeBookFromUserLibrary(userId,bookId);
    }

}
