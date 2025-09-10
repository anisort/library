package com.books.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookSingleItemDto {

    private Long id;

    private String title;

    private String author;

    private String summary;

    private String link;

    private String coverLink;

}
