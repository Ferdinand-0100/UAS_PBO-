package org.example.goajaspring.repository;

import org.example.goajaspring.model.Order;
import org.example.goajaspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}