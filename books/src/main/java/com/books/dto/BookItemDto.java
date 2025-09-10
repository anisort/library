package com.books.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookItemDto {

    private Long id;

    private String title;

    private String author;

    private String coverLink;

}
