package com.books.seeder.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class GutenBookDto {

    private String title;

    private List<AuthorDto> authors;

    private List<String> summaries;

    private Map<String, String> formats;

}
