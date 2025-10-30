package com.books.utils.converters;

import com.books.dto.PagedResponseDto;
import org.springframework.data.domain.Page;

public class PageConverter {

    public static <T> PagedResponseDto<T> convertPageToPagedResponseDto(Page<T> page) {
        PagedResponseDto<T> pagedResponseDto = new PagedResponseDto<>();
        pagedResponseDto.setContent(page.getContent());
        pagedResponseDto.setPageNumber(page.getNumber());
        pagedResponseDto.setPageSize(page.getSize());
        pagedResponseDto.setTotalElements(page.getTotalElements());
        pagedResponseDto.setTotalPages(page.getTotalPages());
        return pagedResponseDto;
    }
}

