package com.books.utils.converters;

import com.books.dto.*;
import com.books.entities.Book;
import com.books.utils.enums.BookStatus;

public class BooksConverter {

    public static BookItemDto convertBookToBookItemDto(Book book) {
        BookItemDto bookItemDto = new BookItemDto();
        bookItemDto.setId(book.getId());
        bookItemDto.setTitle(book.getTitle());
        bookItemDto.setAuthor(book.getAuthor());
        bookItemDto.setCoverLink(book.getCoverLink());
        return bookItemDto;
    }

    public static BookSingleItemDto convertBookToBookSingleItemDto(Book book) {
        BookSingleItemDto bookSingleItemDto = new BookSingleItemDto();
        bookSingleItemDto.setId(book.getId());
        bookSingleItemDto.setTitle(book.getTitle());
        bookSingleItemDto.setAuthor(book.getAuthor());
        bookSingleItemDto.setSummary(book.getSummary());
        bookSingleItemDto.setLink(book.getLink());
        bookSingleItemDto.setCoverLink(book.getCoverLink());
        return bookSingleItemDto;
    }

    public static MyBookItemDto convertBookToMyBookItemDto(Book book, BookStatus bookStatus) {
        MyBookItemDto myBookItemDto = new MyBookItemDto();
        myBookItemDto.setBookItem(convertBookToBookItemDto(book));
        myBookItemDto.setBookStatus(bookStatus);
        return myBookItemDto;
    }

    public static MyBookSingleItemDto convertBookToMyBookSingleItem(Book book, BookStatus bookStatus) {
        MyBookSingleItemDto myBookSingleItemDto = new MyBookSingleItemDto();
        myBookSingleItemDto.setBookSingleItem(convertBookToBookSingleItemDto(book));
        myBookSingleItemDto.setBookStatus(bookStatus);
        return myBookSingleItemDto;
    }

    public static Book convertCreateBookDtoToBook(CreateBookDto createBookDto) {
        Book book = new Book();
        updateBookEntity(book, createBookDto);
        return book;
    }

    public static BookItemDto convertBookSingleToBookItemDto(BookSingleItemDto bookSingleItemDto) {
        BookItemDto bookItemDto = new BookItemDto();
        bookItemDto.setId(bookSingleItemDto.getId());
        bookItemDto.setTitle(bookSingleItemDto.getTitle());
        bookItemDto.setAuthor(bookSingleItemDto.getAuthor());
        bookItemDto.setCoverLink(bookSingleItemDto.getCoverLink());
        return bookItemDto;
    }

    public static void updateBookEntity(Book book, CreateBookDto dto) {
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setSummary(dto.getSummary());
        book.setLink(dto.getLink());
        book.setCoverLink(dto.getCoverLink());
    }
}
