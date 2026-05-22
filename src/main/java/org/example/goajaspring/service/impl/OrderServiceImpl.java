package org.example.goajaspring.service.impl;

import org.example.goajaspring.model.Driver;
import org.example.goajaspring.model.Order;
import org.example.goajaspring.repository.DriverRepository;
import org.example.goajaspring.repository.OrderRepository;
import org.example.goajaspring.service.OrderService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final DriverRepository driverRepository;

    public OrderServiceImpl(OrderRepository orderRepository, DriverRepository driverRepository) {

        this.orderRepository = orderRepository;
        this.driverRepository = driverRepository;
    }


    @Override
    public Order saveOrder(Order order) {

        double tarif = order.getLayanan().getTarifPerKm();

        double totalHarga = order.getJarak() * tarif;

        order.setTotalHarga(totalHarga);

        order.setStatus("MENUNGGU");

        return orderRepository.save(order);
    }
    @Override
    public Order acceptOrder(Long orderId, Long driverId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order tidak ditemukan"));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver tidak ditemukan"));

        if (!driver.isAvailable()) {
            throw new RuntimeException("Driver sedang tidak tersedia");
        }

        order.setDriver(driver);

        order.setStatus("DIJEMPUT");

        driver.setAvailable(false);

        driverRepository.save(driver);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
