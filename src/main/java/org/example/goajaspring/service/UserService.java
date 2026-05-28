package org.example.goajaspring.service;

import org.example.goajaspring.model.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByEmail(String email);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
}