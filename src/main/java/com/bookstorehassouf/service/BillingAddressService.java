package com.bookstorehassouf.service;

import com.bookstorehassouf.model.BillingAddress;
import com.bookstorehassouf.model.UserBilling;


public interface BillingAddressService {
    BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
