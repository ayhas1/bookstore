package com.bookstorehassouf.utility;

import com.bookstorehassouf.model.Order;
import com.bookstorehassouf.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.internet.InternetAddress;
import java.util.Locale;

@Component
public class MailConstructor {

    @Autowired
    private Environment environment;

    @Autowired
    private TemplateEngine templateEngine;

    public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user, String password) {
        String url= contextPath + "/newUser?token="+token;
        String message= "\nPlease click on this link to verify your email and edit your personal information. Your password is: \n"+password;
        SimpleMailMessage email= new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Ayoub's Bookstore - New User");
        email.setText(url+message);
        email.setFrom(environment.getProperty("support.email"));
        return email;
    }

    public MimeMessagePreparator constructOrderConfirmationEmail(User user, Order order, Locale locale) {
        org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
        context.setVariable("order", order);
        context.setVariable("user", user);
        context.setVariable("cartItemList", order.getCartItemList());
        String text = templateEngine.process("orderConfirmationEmailTemplate", context);

        return mimeMessage -> {
            MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
            email.setText(user.getEmail());
            email.setSubject("Order Confirmation - " + order.getId());
            email.setText(text, true);
            email.setFrom(new InternetAddress("ray.deng83@gmail.com"));
        };
    }
}
