package org.example.goajaspring.service;
import org.example.goajaspring.model.User;
import java.util.List;
public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();
}
