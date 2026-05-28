package org.example.goajaspring.service;

import org.example.goajaspring.model.Order;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);

    List<Order> getAllOrders();

    Order acceptOrder(Long orderId, Long driverId);

    Order updateDriverLocation(Long orderId, double lat, double lng);

    Order driverArrived(Long orderId);

    Order getOrderById(Long orderId);
}
