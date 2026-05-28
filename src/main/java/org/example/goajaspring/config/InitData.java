package org.example.goajaspring.config;

import org.example.goajaspring.model.User;
import org.example.goajaspring.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitData {

    @Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            createIfNotExists(userService, "user@example.com",   "Pengguna Biasa", "USER");
            createIfNotExists(userService, "driver@example.com", "Driver Tester",   "DRIVER");
            createIfNotExists(userService, "admin@example.com",  "Administrator",   "ADMIN");
        };
    }

    private void createIfNotExists(UserService userService, String email, String nama, String role) {
        if (userService.findByEmail(email).isEmpty()) {
            User u = new User();
            u.setNama(nama);
            u.setEmail(email);
            u.setPassword("123"); // UserServiceImpl akan meng-encode password
            u.setRole(role);
            try {
                userService.saveUser(u);
                System.out.println("Created account: " + email + " / 123 (role=" + role + ")");
            } catch (RuntimeException ex) {
                System.err.println("Failed to create " + email + " : " + ex.getMessage());
            }
        } else {
            System.out.println("Account already exists: " + email);
        }
    }
}