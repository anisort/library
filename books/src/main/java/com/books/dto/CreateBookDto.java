package com.books.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBookDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Summary is required")
    private String summary;

    @NotBlank(message = "Link to book is required")
    private String link;

    private String coverLink;


}
