package com.interswitch.repository;

import com.interswitch.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingOrAuthorContainingOrGenreContainingOrYearOfPublicationContaining(String keyword, String keyword1, String keyword2, String keyword3);
}