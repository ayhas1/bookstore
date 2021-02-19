package com.bookstorehassouf.service;

import com.bookstorehassouf.model.User;
import com.bookstorehassouf.model.UserBilling;
import com.bookstorehassouf.model.UserPayment;
import com.bookstorehassouf.model.UserShipping;
import com.bookstorehassouf.security.PasswordResetToken;
import com.bookstorehassouf.security.UserRole;

import java.util.Set;


public interface UserService {


    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final User user, final String token);

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    User createUser(User user, Set<UserRole> userRoles);

    User save(User user);

    void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

    void updateUserShipping(UserShipping userShipping, User user);

    void setUserDefaultPayment(Long userPaymentId, User user);

    void setUserDefaultShipping(Long userShippingId, User user);
}

