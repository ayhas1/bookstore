package com.bookstorehassouf;

import com.bookstorehassouf.model.User;
import com.bookstorehassouf.security.Role;
import com.bookstorehassouf.security.UserRole;
import com.bookstorehassouf.service.UserService;
import com.bookstorehassouf.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BookstoreHassoufApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreHassoufApplication.class, args);
    }

    @Override
    public void run(String... args) {
        User user1 = new User();
        user1.setFirstName("Lionel");
        user1.setLastName("Messi");
        user1.setUsername("m");
        user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
        user1.setEmail("Lmessi@gmail.co");
        user1.setTelephone("08353537855");
        Set<UserRole> userRoles = new HashSet<>();
        Role role1 = new Role();
        role1.setRoleId(1L);
        role1.setName("ROLE_USER");
        userRoles.add(new UserRole(user1, role1));

        userService.createUser(user1, userRoles);
    }
}
