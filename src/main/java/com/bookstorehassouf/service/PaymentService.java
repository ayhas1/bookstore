package com.bookstorehassouf.service;


import com.bookstorehassouf.model.Payment;
import com.bookstorehassouf.model.UserPayment;

public interface PaymentService {
    Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
