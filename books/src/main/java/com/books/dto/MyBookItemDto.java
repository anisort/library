package com.books.dto;

import com.books.utils.enums.BookStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyBookItemDto {

    private BookItemDto bookItem;

    private BookStatus bookStatus;

}
