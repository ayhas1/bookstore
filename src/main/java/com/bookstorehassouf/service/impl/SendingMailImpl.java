package com.bookstorehassouf.service.impl;

import com.bookstorehassouf.model.User;
import com.bookstorehassouf.service.SendingMail;
import com.bookstorehassouf.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SendingMailImpl implements SendingMail {

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(HttpServletRequest request, String token, User user, String randomPassword){
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, randomPassword);
        mailSender.send(email);
    }


}
