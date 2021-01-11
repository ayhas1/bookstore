package com.bookstorehassouf.service.impl;

import com.bookstorehassouf.model.User;
import com.bookstorehassouf.service.FirstRandomPassword;
import com.bookstorehassouf.utility.SecurityUtility;
import org.springframework.stereotype.Service;

@Service
public class FirstRandomPasswordImpl implements FirstRandomPassword {
    @Override
    public String firstRandomEncryptedPassword(User user) {

        String randomPassword = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(randomPassword);
        user.setPassword(encryptedPassword);

        return randomPassword;

    }
}
