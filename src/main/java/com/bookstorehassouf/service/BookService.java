package com.bookstorehassouf.service;


import com.bookstorehassouf.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();

    Book findOne(Long id);
}
