package org.example.goajaspring.controller;

import org.example.goajaspring.model.Order;
import org.example.goajaspring.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order saveOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    @PutMapping("/{orderId}/accept/{driverId}")
    public Order acceptOrder(@PathVariable Long orderId,
                             @PathVariable Long driverId) {

        return orderService.acceptOrder(orderId, driverId);
    }
}
