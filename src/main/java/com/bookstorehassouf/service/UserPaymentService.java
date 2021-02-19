package com.bookstorehassouf.service;


import com.bookstorehassouf.model.UserPayment;

public interface UserPaymentService {
    UserPayment findById(Long id);

    void removeById(Long id);
}
