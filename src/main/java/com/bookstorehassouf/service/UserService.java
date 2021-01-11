package com.bookstorehassouf.service;

import com.bookstorehassouf.model.User;
import com.bookstorehassouf.security.PasswordResetToken;
import com.bookstorehassouf.security.UserRole;

import java.util.Set;


public interface UserService {


    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final User user, final String token);

    User findByUsername(String username);

    User findByEmail(String email);

    User createUser(User user, Set<UserRole> userRoles);

    User save(User user);
}
