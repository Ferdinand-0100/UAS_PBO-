package org.example.goajaspring.controller;

import org.example.goajaspring.model.Driver;
import org.example.goajaspring.model.Layanan;
import org.example.goajaspring.model.Order;
import org.example.goajaspring.service.DriverService;
import org.example.goajaspring.service.LayananService;
import org.example.goajaspring.service.OrderService;
import org.example.goajaspring.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Controller
public class PageController {

    private final OrderService orderService;
    private final DriverService driverService;
    private final LayananService layananService;
    private final UserService userService;

    public PageController(OrderService orderService,
                          DriverService driverService,
                          LayananService layananService,
                          UserService userService) {
        this.orderService  = orderService;
        this.driverService = driverService;
        this.layananService = layananService;
        this.userService   = userService;
    }

    /* DASHBOARD — GET */
    @GetMapping("/")
    public String dashboard(Model model, Authentication authentication) {
        model.addAttribute("layananList", layananService.getAllLayanan());
        model.addAttribute("orderCount",  orderService.getAllOrders().size());
        model.addAttribute("driverCount", driverService.getAllDrivers().size());

        // Determine role for template in a null-safe way
        String currentRole = "USER";
        if (authentication != null) {
            var authorities = authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                // get first authority string, e.g. "ROLE_ADMIN"
                String full = authorities.iterator().next().getAuthority();
                if (full != null) {
                    if (full.startsWith("ROLE_")) {
                        currentRole = full.substring(5);
                    } else {
                        currentRole = full;
                    }
                }
            }
        }
        model.addAttribute("currentRole", currentRole);

        // build tariff map for JS (layananId -> tarifPerKm)
        Map<Long, Double> tariffMap = new HashMap<>();
        for (Layanan l : layananService.getAllLayanan()) {
            if (l.getId() != null) {
                tariffMap.put(l.getId(), l.getTarifPerKm());
            }
        }
        model.addAttribute("tariffMap", tariffMap);

        return "index";
    }

    /* ORDERS — GET /orders */
    @GetMapping("/orders")
    public String orders(Model model, Authentication authentication) {
        model.addAttribute("orders", orderService.getAllOrders());

        String currentRole = "USER";
        if (authentication != null) {
            var authorities = authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                String full = authorities.iterator().next().getAuthority();
                if (full != null) {
                    currentRole = full.startsWith("ROLE_") ? full.substring(5) : full;
                }
            }
        }
        model.addAttribute("currentRole", currentRole);
        return "orders";
    }

    /* ACCEPT ORDER — POST /orders/{id}/accept/{driverId} */
    @PostMapping("/orders/{orderId}/accept/{driverId}")
    public String acceptOrder(@PathVariable Long orderId,
                              @PathVariable Long driverId,
                              RedirectAttributes ra) {
        try {
            orderService.acceptOrder(orderId, driverId);
            ra.addFlashAttribute("success", "Order berhasil diterima oleh driver.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    /* DRIVERS — GET /drivers */
    @GetMapping("/drivers")
    public String drivers(Model model, Authentication authentication) {
        model.addAttribute("drivers", driverService.getAllDrivers());
        model.addAttribute("newDriver", new Driver());

        String currentRole = "USER";
        if (authentication != null) {
            var authorities = authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                String full = authorities.iterator().next().getAuthority();
                if (full != null) {
                    currentRole = full.startsWith("ROLE_") ? full.substring(5) : full;
                }
            }
        }
        model.addAttribute("currentRole", currentRole);
        return "drivers";
    }

    /* ADD DRIVER — POST /drivers */
    @PostMapping("/drivers")
    public String addDriver(@ModelAttribute Driver driver,
                            RedirectAttributes ra) {
        driver.setRole("DRIVER");
        driver.setAvailable(true);
        driverService.saveDriver(driver);
        ra.addFlashAttribute("success", "Driver " + driver.getNama() + " berhasil ditambahkan.");
        return "redirect:/drivers";
    }

    /* LAYANAN — GET /layanan */
    @GetMapping("/layanan")
    public String layanan(Model model, Authentication authentication) {
        model.addAttribute("layananList", layananService.getAllLayanan());
        model.addAttribute("newLayanan", new Layanan());

        String currentRole = "USER";
        if (authentication != null) {
            var authorities = authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                String full = authorities.iterator().next().getAuthority();
                if (full != null) {
                    currentRole = full.startsWith("ROLE_") ? full.substring(5) : full;
                }
            }
        }
        model.addAttribute("currentRole", currentRole);
        return "layanan";
    }

    /* ADD LAYANAN — POST /layanan */
    @PostMapping("/layanan")
    public String addLayanan(@ModelAttribute Layanan layanan,
                             RedirectAttributes ra) {
        layananService.saveLayanan(layanan);
        ra.addFlashAttribute("success", "Layanan " + layanan.getNamaLayanan() + " berhasil ditambahkan.");
        return "redirect:/layanan";
    }

    /* SUBMIT ORDER — POST /order/submit (from the dashboard form)*/
    @PostMapping("/order/submit")
    public String submitOrder(@RequestParam String lokasiJemput,
                              @RequestParam String lokasiTujuan,
                              @RequestParam Long layananId,
                              @RequestParam double jarak,
                              RedirectAttributes ra,
                              Authentication authentication) {

        try {
            List<Layanan> layananList = layananService.getAllLayanan();
            Layanan layanan = layananList.stream()
                    .filter(l -> l.getId().equals(layananId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Layanan tidak ditemukan"));

            Order order = new Order(
                    lokasiJemput,
                    lokasiTujuan,
                    jarak,
                    0,           // totalHarga calculated in service
                    "MENUNGGU",
                    null,        // user — not linked to auth user yet
                    null,        // driver — assigned later via acceptOrder
                    layanan
            );

            if (authentication != null) {
                userService.findByEmail(authentication.getName()).ifPresent(order::setUser);
            }

            orderService.saveOrder(order);
            ra.addFlashAttribute("success", "Pesanan berhasil dibuat! Status: MENUNGGU.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", "Gagal membuat pesanan: " + e.getMessage());
        }
        return "redirect:/";

    }

    @GetMapping("/track/{orderId}")
    public String trackPage(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "track";
    }
}
