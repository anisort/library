package com.books.controllers;

import com.books.dto.BookItemDto;
import com.books.dto.BookItemListDto;
import com.books.dto.BookSingleItemDto;
import com.books.dto.PagedResponseDto;
import com.books.services.books.all.PublicBooksService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/books")
public class PublicBooksController {

    private final PublicBooksService publicBooksService;

    public PublicBooksController(PublicBooksService publicBooksService) {
        this.publicBooksService = publicBooksService;
    }

    @GetMapping()
    public PagedResponseDto<BookItemDto> getAllBooks(Pageable pageable, @RequestParam(required = false) String letter) {
        return publicBooksService.getAllBooks(pageable, letter);
    }

    @GetMapping("/top")
    public BookItemListDto getTopBooks(@RequestParam(defaultValue = "8") int limit) {
        return publicBooksService.getTopBooks(limit);
    }

    @GetMapping("/search")
    public List<BookItemDto> searchBooks(@RequestParam(defaultValue = "title") String filter, @RequestParam(defaultValue = "") String searchText) {
        return publicBooksService.searchBooks(filter, searchText);
    }

    @GetMapping("{id}")
    public BookSingleItemDto getBookById(@PathVariable("id") Long id) {
        return publicBooksService.getBookById(id);
    }
}
