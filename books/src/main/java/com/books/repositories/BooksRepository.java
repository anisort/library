package com.books.repositories;

import com.books.entities.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleStartingWithIgnoreCase(String letter, Pageable pageable);

    List<Book> findTop10ByTitleContainingIgnoreCase(String title);

    List<Book> findTop10ByAuthorContainingIgnoreCase(String author);

}
