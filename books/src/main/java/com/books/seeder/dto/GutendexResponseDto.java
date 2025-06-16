package com.books.seeder.dto;

import java.util.List;

public class GutendexResponseDto {

    private int count;

    private String next;

    private String previous;

    private List<GutenBookDto> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<GutenBookDto> getResults() {
        return results;
    }

    public void setResults(List<GutenBookDto> results) {
        this.results = results;
    }
}
