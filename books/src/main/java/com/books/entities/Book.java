package com.books.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String title;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column(columnDefinition = "TEXT")
    private String coverLink;

    public Book() {}

    public Book(String title, String author, String summary, String link) {
        this.title = title;
        this.author = author;
        this.summary = summary;
        this.link = link;
    }

    public Book(Long id, String title, String author, String summary, String link, String coverLink) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.summary = summary;
        this.link = link;
        this.coverLink = coverLink;
    }

}
