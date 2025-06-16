package com.books.dto;

import com.books.utils.enums.BookStatus;

public class MyBookItemDto {

    private BookItemDto bookItem;

    private BookStatus bookStatus;

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public BookItemDto getBookItem() {
        return bookItem;
    }

    public void setBookItem(BookItemDto bookItem) {
        this.bookItem = bookItem;
    }
}
