package com.books.seeder.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GutendexResponseDto {

    private int count;

    private String next;

    private String previous;

    private List<GutenBookDto> results;

}
