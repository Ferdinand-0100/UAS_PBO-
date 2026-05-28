package org.example.goajaspring.service;
import org.example.goajaspring.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    List<User> getAllUsers();
    Optional<User> findByEmail(String email);
}
