package com.books.dto;

import com.books.utils.enums.BookStatus;
import jakarta.validation.constraints.NotNull;

public class AddBookStatusDto {

    @NotNull(message = "Status is required")
    private BookStatus bookStatus;


    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }
}
