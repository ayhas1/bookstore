package com.bookstorehassouf.repository;

import com.bookstorehassouf.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {
}
