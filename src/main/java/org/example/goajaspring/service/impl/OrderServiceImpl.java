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

    private static final double AVERAGE_SPEED_KMH = 30.0;

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

    @Override
    public Order updateDriverLocation(Long orderId, double lat, double lng) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order tidak ditemukan"));
        order.setDriverLat(lat);
        order.setDriverLng(lng);

        // compute ETA if tujuan coords available
        if (order.getLokasiTujuanLat() != null && order.getLokasiTujuanLng() != null) {
            double km = distanceInKm(lat, lng, order.getLokasiTujuanLat(), order.getLokasiTujuanLng());
            int etaMinutes = (int) Math.round((km / AVERAGE_SPEED_KMH) * 60);
            order.setEstimatedArrivalMinutes(Math.max(1, etaMinutes));
        }

        return orderRepository.save(order);
    }

    @Override
    public Order driverArrived(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order tidak ditemukan"));

        order.setStatus("SAMPAI");

        Driver driver = order.getDriver();
        if (driver != null) {
            driver.setAvailable(true);
            driverRepository.save(driver);
        }

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order tidak ditemukan"));
    }

    // Haversine formula
    private double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius Earth km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2)
                        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}