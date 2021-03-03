package com.bookstorehassouf.service;


import com.bookstorehassouf.model.ShippingAddress;
import com.bookstorehassouf.model.UserShipping;

public interface ShippingAddressService {
    ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
