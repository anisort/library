package com.books.dto;

import com.books.utils.enums.BookStatus;

public class MyBookSingleItemDto {

    private BookSingleItemDto bookSingleItem;

    private BookStatus bookStatus;

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }


    public BookSingleItemDto getBookSingleItem() {
        return bookSingleItem;
    }

    public void setBookSingleItem(BookSingleItemDto bookSingleItem) {
        this.bookSingleItem = bookSingleItem;
    }
}
