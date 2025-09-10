package com.books.entities;

import com.books.utils.enums.BookStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_books")
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public UserBook() {}

    public UserBook(Long userId, Book book,  BookStatus bookStatus) {
        this.userId = userId;
        this.book = book;
        this.bookStatus = bookStatus;
    }

}
