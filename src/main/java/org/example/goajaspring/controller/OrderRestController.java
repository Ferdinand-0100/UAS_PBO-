package org.example.goajaspring.controller;

import org.example.goajaspring.model.Order;
import org.example.goajaspring.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.example.goajaspring.model.Driver;
import org.example.goajaspring.repository.DriverRepository;
import org.example.goajaspring.repository.OrderRepository;
import org.example.goajaspring.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.example.goajaspring.model.User;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderRestController(OrderService orderService,
                               DriverRepository driverRepository,
                               OrderRepository orderRepository,
                               UserRepository userRepository) {
        this.orderService = orderService;
        this.driverRepository = driverRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{id}/location")
    public Order updateLocation(@PathVariable Long id, @RequestBody LocationDto dto) {
        return orderService.updateDriverLocation(id, dto.getLat(), dto.getLng());
    }

    @PostMapping("/{id}/driver-location")
    public Order updateDriverLocation(@PathVariable Long id, @RequestBody LocationDto dto) {
        return orderService.updateDriverLocation(id, dto.getLat(), dto.getLng());
    }

    @PostMapping("/{id}/user-location")
    public Order updateUserLocation(@PathVariable Long id, @RequestBody LocationDto dto) {
        return orderService.updateUserLocation(id, dto.getLat(), dto.getLng());
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
        t.setUserLat(o.getUserLat());
        t.setUserLng(o.getUserLng());
        t.setPickupLat(o.getLokasiJemputLat());
        t.setPickupLng(o.getLokasiJemputLng());
        t.setDestinationLat(o.getLokasiTujuanLat());
        t.setDestinationLng(o.getLokasiTujuanLng());
        t.setEtaMinutes(o.getEstimatedArrivalMinutes());
        t.setStatus(o.getStatus());
        return t;
    }

    @PostMapping("/{id}/accept")
    public Order acceptOrderSimple(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Driver tidak ter-autentikasi");
        }

        // Query DriverRepository directly instead of UserRepository
        Driver driver = driverRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Driver tidak ditemukan: " + userDetails.getUsername()));

        if (!driver.isAvailable()) {
            throw new RuntimeException("Driver sedang tidak tersedia");
        }

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order tidak ditemukan"));

        order.setDriver(driver);
        order.setStatus("DIJEMPUT");
        driver.setAvailable(false);
        driverRepository.save(driver);

        return orderRepository.save(order);
    }

    @PostMapping("/{id}/update-status")
    public Order updateOrderStatusEndpoint(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        return orderService.updateOrderStatus(id, payload.get("status"));
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
        private Double userLat;
        private Double userLng;
        private Double pickupLat;
        private Double pickupLng;
        private Double destinationLat;
        private Double destinationLng;
        private Integer etaMinutes;
        private String status;

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public Double getDriverLat() { return driverLat; }
        public void setDriverLat(Double driverLat) { this.driverLat = driverLat; }
        public Double getDriverLng() { return driverLng; }
        public void setDriverLng(Double driverLng) { this.driverLng = driverLng; }
        public Double getUserLat() { return userLat; }
        public void setUserLat(Double userLat) { this.userLat = userLat; }
        public Double getUserLng() { return userLng; }
        public void setUserLng(Double userLng) { this.userLng = userLng; }
        public Double getPickupLat() { return pickupLat; }
        public void setPickupLat(Double pickupLat) { this.pickupLat = pickupLat; }
        public Double getPickupLng() { return pickupLng; }
        public void setPickupLng(Double pickupLng) { this.pickupLng = pickupLng; }
        public Double getDestinationLat() { return destinationLat; }
        public void setDestinationLat(Double destinationLat) { this.destinationLat = destinationLat; }
        public Double getDestinationLng() { return destinationLng; }
        public void setDestinationLng(Double destinationLng) { this.destinationLng = destinationLng; }
        public Integer getEtaMinutes() { return etaMinutes; }
        public void setEtaMinutes(Integer etaMinutes) { this.etaMinutes = etaMinutes; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
