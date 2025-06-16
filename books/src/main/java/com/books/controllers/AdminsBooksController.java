package com.books.controllers;

import com.books.dto.BookSingleItemDto;
import com.books.dto.CreateBookDto;
import com.books.services.books.admins.IAdminsBooksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/books")
public class AdminsBooksController {

    private final IAdminsBooksService adminsBooksService;

    @Autowired
    public AdminsBooksController(IAdminsBooksService adminsBooksService) {
        this.adminsBooksService = adminsBooksService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BookSingleItemDto createBook(@RequestPart("book") CreateBookDto createBookDto, @RequestPart("file") MultipartFile file) throws IOException {
        return adminsBooksService.createBook(createBookDto, file);
    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BookSingleItemDto updateBookWithFile(@PathVariable("id") Long id, @RequestPart("book") @Valid CreateBookDto createBookDto, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return adminsBooksService.updateBook(id, createBookDto, file);
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        adminsBooksService.deleteBook(id);
    }
}
