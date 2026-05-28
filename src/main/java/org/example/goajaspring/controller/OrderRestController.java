package org.example.goajaspring.controller;

import org.example.goajaspring.model.Order;
import org.example.goajaspring.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{id}/location")
    public Order updateLocation(@PathVariable Long id, @RequestBody LocationDto dto) {
        return orderService.updateDriverLocation(id, dto.getLat(), dto.getLng());
    }

    @PostMapping("/{id}/arrived")
    public Order driverArrived(@PathVariable Long id) {
        return orderService.driverArrived(id);
    }

    @GetMapping("/{id}/track")
    public TrackDto track(@PathVariable Long id) {
        Order o = orderService.getOrderById(id);
        TrackDto t = new TrackDto();
        t.setOrderId(o.getId());
        t.setDriverLat(o.getDriverLat());
        t.setDriverLng(o.getDriverLng());
        t.setEtaMinutes(o.getEstimatedArrivalMinutes());
        t.setStatus(o.getStatus());
        return t;
    }

    // DTOs
    public static class LocationDto {
        private Double lat;
        private Double lng;

        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        public Double getLng() { return lng; }
        public void setLng(Double lng) { this.lng = lng; }
    }

    public static class TrackDto {
        private Long orderId;
        private Double driverLat;
        private Double driverLng;
        private Integer etaMinutes;
        private String status;

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public Double getDriverLat() { return driverLat; }
        public void setDriverLat(Double driverLat) { this.driverLat = driverLat; }
        public Double getDriverLng() { return driverLng; }
        public void setDriverLng(Double driverLng) { this.driverLng = driverLng; }
        public Integer getEtaMinutes() { return etaMinutes; }
        public void setEtaMinutes(Integer etaMinutes) { this.etaMinutes = etaMinutes; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
