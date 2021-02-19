package com.bookstorehassouf.service.impl;


import com.bookstorehassouf.model.UserPayment;
import com.bookstorehassouf.repository.UserPaymentRepository;
import com.bookstorehassouf.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    public UserPayment findById(Long id) {
        return userPaymentRepository.findById(id).get();
    }

    public void removeById(Long id) {
        userPaymentRepository.deleteById(id);
    }
}
