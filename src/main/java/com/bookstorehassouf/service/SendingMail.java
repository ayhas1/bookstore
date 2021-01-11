package com.bookstorehassouf.service;

import com.bookstorehassouf.model.User;

import javax.servlet.http.HttpServletRequest;

public interface SendingMail {
    void sendEmail(HttpServletRequest request, String token, User user, String randomPassword);
}
