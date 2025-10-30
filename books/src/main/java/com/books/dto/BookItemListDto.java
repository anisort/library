package com.books.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class BookItemListDto implements Serializable {
    private List<BookItemDto> books;
    private int limit;
}
