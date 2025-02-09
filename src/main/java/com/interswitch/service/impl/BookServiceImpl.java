package com.interswitch.service.impl;

import com.interswitch.entity.Book;
import com.interswitch.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl {
    private final BookRepository bookRepository;

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> searchBook(String keyword) {
        // search by title, author, genre, or year of publication
        return bookRepository.findByTitleContainingOrAuthorContainingOrGenreContainingOrYearContaining(keyword, keyword, keyword, keyword);
    }
}
