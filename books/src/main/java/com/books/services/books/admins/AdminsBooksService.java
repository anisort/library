package com.books.services.books.admins;

import com.books.dto.BookSingleItemDto;
import com.books.dto.CreateBookDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminsBooksService {

    BookSingleItemDto createBook(CreateBookDto createBookDto, MultipartFile file) throws IOException;

    BookSingleItemDto updateBook(Long id, CreateBookDto updateBookDto, MultipartFile newFile)  throws IOException;

    void deleteBook(Long id);
}
