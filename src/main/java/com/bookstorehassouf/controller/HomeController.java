package com.bookstorehassouf.controller;


import com.bookstorehassouf.model.User;
import com.bookstorehassouf.security.PasswordResetToken;
import com.bookstorehassouf.security.Role;
import com.bookstorehassouf.security.UserRole;
import com.bookstorehassouf.service.SendingMail;
import com.bookstorehassouf.service.impl.FirstRandomPasswordImpl;
import com.bookstorehassouf.service.impl.UserSecurityService;
import com.bookstorehassouf.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


@Controller
public class HomeController {

    @Autowired
    private SendingMail sendingMail;

    @Autowired
    private FirstRandomPasswordImpl firstRandomPassword;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", true);
        return "myAccount";
    }

    //creates new user Account
    @PostMapping("/newUser")
    public String newUserPost(HttpServletRequest request,
                              @ModelAttribute("email") String userEmail,
                              @ModelAttribute("username") String username,
                              Model model) {

        model.addAttribute("classActiveNewAccount", true);
        model.addAttribute("email", userEmail);
        model.addAttribute("username", username);

        //check if the username and email already exist
        if (userService.findByUsername(username) != null) {
            model.addAttribute("usernameExists", true);

            return "myAccount";
        }

        if (userService.findByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);

            return "myAccount";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);

        //generate a random password the first time
        String randomPassword = firstRandomPassword.firstRandomEncryptedPassword(user);

        //setting the role of the user
        Role role = new Role();
        role.setRoleId(1L);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        //generate reset token
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        //email reset token sending
        sendingMail.sendEmail(request, token, user, randomPassword);

        model.addAttribute("emailSent", true);

        return "myAccount";

    }

    @GetMapping("/newUser")
    public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
        PasswordResetToken passToken = userService.getPasswordResetToken(token);

        if (passToken == null) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/badRequest";
        }
        //If the User is found retrieve it
        User user = passToken.getUser();
        String username = user.getUsername();

        //Making sure that the active session is for this current user
        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        model.addAttribute("user", user);
        model.addAttribute("classActiveEdit", true);
        return "myProfile";
    }

    @RequestMapping("/forgetPassword")
    public String forgetPassword(HttpServletRequest request,
                                 @ModelAttribute("email") String userEmail,
                                 @ModelAttribute("username") String username,
                                 Model model) {
        model.addAttribute("classActiveForgetPassword", true);
        User user= userService.findByEmail(userEmail);

        if(user == null){
            model.addAttribute("emailNotExist", true);
            return "myAccount";
        }

        //generate a random password the first time
        String randomPassword = firstRandomPassword.firstRandomEncryptedPassword(user);

        userService.save(user);

        //generate reset token
       String token = UUID.randomUUID().toString();
       userService.createPasswordResetTokenForUser(user,token);

        //email reset token sending
        sendingMail.sendEmail(request, token, user, randomPassword);

        model.addAttribute("forgetPasswordEmailSent", true);

        return "myAccount";
    }

}
