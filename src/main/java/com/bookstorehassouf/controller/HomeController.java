package com.bookstorehassouf.controller;


import com.bookstorehassouf.model.*;
import com.bookstorehassouf.security.PasswordResetToken;
import com.bookstorehassouf.security.Role;
import com.bookstorehassouf.security.UserRole;
import com.bookstorehassouf.service.BookService;
import com.bookstorehassouf.service.SendingMail;
import com.bookstorehassouf.service.UserPaymentService;
import com.bookstorehassouf.service.UserShippingService;
import com.bookstorehassouf.service.impl.FirstRandomPasswordImpl;
import com.bookstorehassouf.service.impl.UserSecurityService;
import com.bookstorehassouf.service.impl.UserServiceImpl;
import com.bookstorehassouf.utility.DEConstants;
import com.bookstorehassouf.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;


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

    @Autowired
    private BookService bookService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private UserPaymentService userPaymentService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", true);
        return "myAccount";
    }

    //Browse the books for the user
    @RequestMapping("/bookshelf")
    public String bookshelf(Model model, Principal principal){
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        List<Book> bookList= bookService.findAll();
        model.addAttribute("booklist", bookList);
        return "bookshelf";
    }

    //show details of the book for the logged in user
    @RequestMapping(value = "/bookDetail/{id}", method = RequestMethod.GET)
    public String bookDetail(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty", 1);

        return "bookDetail";
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

    //myProfile Controller
    @RequestMapping("/myProfile")
    public String myProfile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        //model.addAttribute("orderList", user.getOrderList());

        UserShipping userShipping = new UserShipping();
        model.addAttribute("userShipping", userShipping);

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("listOfShippingAddresses", true);

        List<String> stateList = DEConstants.listOfDEStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("classActiveEdit", true);

        return "myProfile";
    }

    //list of credit cards the user have
    @RequestMapping("/listOfCreditCards")
    public String listOfCreditCards(Model model, Principal principal, HttpServletRequest request) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        //model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }

    //list of shipping addresses the user have
    @RequestMapping("/listOfShippingAddresses")
    public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        //model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }

    //where to add a new credit card to the user's profile
    @RequestMapping("/addNewCreditCard")
    public String addNewCreditCard(
            Model model, Principal principal
    ){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("addNewCreditCard", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        UserBilling userBilling = new UserBilling();
        UserPayment userPayment = new UserPayment();

        model.addAttribute("userBilling", userBilling);
        model.addAttribute("userPayment", userPayment);

        List<String> stateList = DEConstants.listOfDEStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        //model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    //where to add a new shipping address to the user's profile
    @RequestMapping("/addNewShippingAddress")
    public String addNewShippingAddress(
            Model model, Principal principal
    ){
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);

        UserShipping userShipping = new UserShipping();

        model.addAttribute("userShipping", userShipping);

        List<String> stateList = DEConstants.listOfDEStatesCode;
        Collections.sort(stateList);
        model.addAttribute("stateList", stateList);
        model.addAttribute("userShippingList", user.getUserShippingList());
        //model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    //add a new credit card to the user's profile
    @RequestMapping(value = "/addNewCreditCard", method = RequestMethod.POST)
    public String addNewCreditCard(
            @ModelAttribute("userPayment") UserPayment userPayment,
            @ModelAttribute("userBilling") UserBilling userBilling,
            Principal principal, Model model
    ){
        User user = userService.findByUsername(principal.getName());
        userService.updateUserBilling(userBilling, userPayment, user);

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        //model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    //add a new shipping address to the user's profile
    @RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.POST)
    public String addNewShippingAddress(
            @ModelAttribute("userShipping") UserShipping userShipping,
            Principal principal, Model model
    ){
        User user = userService.findByUsername(principal.getName());
        userService.updateUserShipping(userShipping, user);

        model.addAttribute("user", user);
        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        //model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @RequestMapping("/updateCreditCard")
    public String updateCreditCard(
            @ModelAttribute("id") Long creditCardId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(!user.getId().equals(userPayment.getUser().getId())) {
            return "badRequestPage";
        }
        else {
            model.addAttribute("user", user);
            UserBilling userBilling = userPayment.getUserBilling();
            model.addAttribute("userPayment", userPayment);
            model.addAttribute("userBilling", userBilling);

            List<String> stateList = DEConstants.listOfDEStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            model.addAttribute("addNewCreditCard", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddress", true);
            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            //model.addAttribute("orderList", user.getOrderList());
            return "myProfile";
        }
    }

    //update user shipping
    @RequestMapping("/updateUserShipping")
    public String updateUserShipping(
            @ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(shippingAddressId);

        if(!user.getId().equals(userShipping.getUser().getId())) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("userShipping", userShipping);

            List<String> stateList = DEConstants.listOfDEStatesCode;
            Collections.sort(stateList);
            model.addAttribute("stateList", stateList);

            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfCreditCards", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
            //model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    @RequestMapping(value="/setDefaultPayment", method=RequestMethod.POST)
    public String setDefaultPayment(
            @ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultPayment(defaultPaymentId, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        //model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
    public String setDefaultShippingAddress(
            @ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultShipping(defaultShippingId, user);

        model.addAttribute("user", user);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        model.addAttribute("userPaymentList", user.getUserPaymentList());
        model.addAttribute("userShippingList", user.getUserShippingList());
        //model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    //remove credit card
    @RequestMapping("/removeCreditCard")
    public String removeCreditCard(
            @ModelAttribute("id") Long creditCardId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserPayment userPayment = userPaymentService.findById(creditCardId);

        if(user.getId() != userPayment.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);
            userPaymentService.removeById(creditCardId);

            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
           // model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    //remove the user's shipping
    @RequestMapping("/removeUserShipping")
    public String removeUserShipping(
            @ModelAttribute("id") Long userShippingId, Principal principal, Model model
    ) {
        User user = userService.findByUsername(principal.getName());
        UserShipping userShipping = userShippingService.findById(userShippingId);

        if(user.getId() != userShipping.getUser().getId()) {
            return "badRequestPage";
        } else {
            model.addAttribute("user", user);

            userShippingService.removeById(userShippingId);

            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("classActiveShipping", true);

            model.addAttribute("userPaymentList", user.getUserPaymentList());
            model.addAttribute("userShippingList", user.getUserShippingList());
           // model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
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

    //Update the user's infos
    @RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
    public String updateUserInfo(
            @ModelAttribute("user") User user,
            @ModelAttribute("newPassword") String newPassword,
            Model model
    ) throws Exception {
        User currentUser = userService.findById(user.getId());

        if(currentUser == null) {
            throw new Exception ("User not found");
        }

//        check email already exists
        if(userService.findByEmail(user.getEmail()) != null) {
            if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
                model.addAttribute("emailExists", true);
                return "myProfile";
            }
        }

//        check username already exists
        if(userService.findByUsername(user.getUsername()) != null) {
            if(userService.findByUsername(user.getUsername()).getId() !=currentUser.getId()) {
                model.addAttribute("usernameExists", true);
                return "myProfile";
            }
        }

//        update password
        if(newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
            BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            String dbPassword = currentUser.getPassword();
            if(passwordEncoder.matches(user.getPassword(), dbPassword)) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("incorrectPassword", true);

                return "myProfile";
            }
        }

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());

        userService.save(currentUser);

        model.addAttribute("updateSuccess", true);
        model.addAttribute("user", currentUser);
        model.addAttribute("classActiveEdit", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("listOfCreditCards", true);

        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

}
