package com.bookstorehassouf.service.impl;

import com.bookstorehassouf.model.Book;
import com.bookstorehassouf.repository.BookRepository;
import com.bookstorehassouf.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findOne(Long id) {
        Optional<Book> bookResponse = bookRepository.findById(id);
        Book book = bookResponse.get();
        return book;
    }
}
