package com.books.repositories;

import com.books.entities.UserBook;
import com.books.repositories.projections.TopBookProjection;
import com.books.utils.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBooksRepository extends JpaRepository<UserBook, Long> {

    Optional<UserBook> findByUserIdAndBookId(Long userId, Long bookId);

    Page<UserBook> findAllByUserId(Long userId, Pageable pageable);

    Page<UserBook> findAllByUserIdAndBookStatus(Long userId, BookStatus bookStatus, Pageable pageable);

    @Query("SELECT ub.book AS book, COUNT(ub) AS count " +
            "FROM UserBook ub " +
            "GROUP BY ub.book " +
            "ORDER BY count DESC")
    List<TopBookProjection> findTopBooks(Pageable pageable);

}
