package com.books.dto;

import com.books.utils.enums.BookStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddBookStatusDto {

    @NotNull(message = "Status is required")
    private BookStatus bookStatus;


}
