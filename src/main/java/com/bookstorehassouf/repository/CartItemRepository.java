package com.bookstorehassouf.repository;

import com.bookstorehassouf.model.CartItem;
import com.bookstorehassouf.model.Order;
import com.bookstorehassouf.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

    List<CartItem> findByOrder(Order order);
}
