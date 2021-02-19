package com.bookstorehassouf.service;


import com.bookstorehassouf.model.UserShipping;

public interface UserShippingService {
    UserShipping findById(Long id);

    void removeById(Long id);
}
