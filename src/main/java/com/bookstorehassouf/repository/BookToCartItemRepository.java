package com.bookstorehassouf.repository;

import com.bookstorehassouf.model.BookToCartItem;
import com.bookstorehassouf.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BookToCartItemRepository extends JpaRepository<BookToCartItem, Long> {
    void deleteByCartItem(CartItem cartItem);
}
