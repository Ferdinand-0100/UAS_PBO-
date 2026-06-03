package org.example.goajaspring.controller;

import org.example.goajaspring.model.Order;
import org.example.goajaspring.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
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
    @GetMapping("/{id}")
    public Map<String, Object> getOrderById(@PathVariable Long id) {
        Order o = orderService.getOrderById(id);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("id", o.getId());
        result.put("lokasiJemput", o.getLokasiJemput());
        result.put("lokasiTujuan", o.getLokasiTujuan());
        result.put("lokasiJemputLat", o.getLokasiJemputLat());
        result.put("lokasiJemputLng", o.getLokasiJemputLng());
        result.put("lokasiTujuanLat", o.getLokasiTujuanLat());
        result.put("lokasiTujuanLng", o.getLokasiTujuanLng());
        result.put("jarak", o.getJarak());
        result.put("totalHarga", o.getTotalHarga());
        result.put("status", o.getStatus());
        result.put("driverLat", o.getDriverLat());
        result.put("driverLng", o.getDriverLng());
        result.put("userLat", o.getUserLat());
        result.put("userLng", o.getUserLng());
        result.put("estimatedArrivalMinutes", o.getEstimatedArrivalMinutes());
        result.put("layananNama", o.getLayanan() != null ? o.getLayanan().getNamaLayanan() : null);
        result.put("driverNama", o.getDriver() != null ? o.getDriver().getNama() : null);
        return result;
    }
}
