package com.books.dto;

import jakarta.validation.constraints.NotBlank;

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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverUrl) {
        this.coverLink = coverUrl;
    }
}
