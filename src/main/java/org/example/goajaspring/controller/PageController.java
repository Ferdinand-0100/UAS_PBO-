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
import org.example.goajaspring.repository.OrderRepository;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Controller
public class PageController {

    private final OrderService orderService;
    private final DriverService driverService;
    private final LayananService layananService;
    private final UserService userService;
    private final OrderRepository orderRepository;  // <-- tambah ini

    public PageController(OrderService orderService,
                          DriverService driverService,
                          LayananService layananService,
                          UserService userService,
                          OrderRepository orderRepository) {  // <-- tambah param
        this.orderService  = orderService;
        this.driverService = driverService;
        this.layananService = layananService;
        this.userService   = userService;
        this.orderRepository = orderRepository;  // <-- assign
    }

    /* DASHBOARD — GET */
    @GetMapping("/")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication != null) {
            var authorities = authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                String full = authorities.iterator().next().getAuthority();
                if (full != null) {
                    String role = full.startsWith("ROLE_") ? full.substring(5) : full;
                    if ("DRIVER".equals(role)) {
                        return "redirect:/orders";
                    } else if ("ADMIN".equals(role)) {
                        return "redirect:/layanan";
                    }
                }
            }
        }

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
        String currentRole = "USER";
        List<Order> ordersList = new ArrayList<>();

        if (authentication != null) {
            var authorities = authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                String full = authorities.iterator().next().getAuthority();
                if (full != null) {
                    currentRole = full.startsWith("ROLE_") ? full.substring(5) : full;
                }
            }

            // Jika USER, tampilkan hanya pesanan miliknya
            if ("USER".equals(currentRole)) {
                var userOpt = userService.findByEmail(authentication.getName());
                if (userOpt.isPresent()) {
                    ordersList = orderRepository.findByUser(userOpt.get());
                }
            } else {
                // DRIVER dan ADMIN melihat semua
                ordersList = orderService.getAllOrders();
            }
        }

        model.addAttribute("orders", ordersList);
        model.addAttribute("currentRole", currentRole);
        return "orders";
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

        if (authentication != null) {
            var authorities = authentication.getAuthorities();
            if (authorities != null && !authorities.isEmpty()) {
                String full = authorities.iterator().next().getAuthority();
                if (full != null) {
                    String role = full.startsWith("ROLE_") ? full.substring(5) : full;
                    if ("DRIVER".equals(role)) {
                        ra.addFlashAttribute("error", "Gagal membuat pesanan: Driver tidak diperbolehkan membuat pesanan.");
                        return "redirect:/orders";
                    } else if ("ADMIN".equals(role)) {
                        ra.addFlashAttribute("error", "Gagal membuat pesanan: Admin tidak diperbolehkan membuat pesanan.");
                        return "redirect:/layanan";
                    }
                }
            }
        }

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

    /* DELETE DRIVER — POST /drivers/{id}/delete */
    @PostMapping("/drivers/{driverId}/delete")
    public String deleteDriver(@PathVariable Long driverId, RedirectAttributes ra) {
        try {
            // cari nama driver (opsional) agar pesan flash lebih informatif
            String driverName = driverService.getAllDrivers().stream()
                    .filter(d -> d.getId() != null && d.getId().equals(driverId))
                    .map(d -> d.getNama())
                    .findFirst()
                    .orElse(null);

            driverService.deleteDriver(driverId);

            if (driverName != null) {
                ra.addFlashAttribute("success", "Driver " + driverName + " berhasil dihapus.");
            } else {
                ra.addFlashAttribute("success", "Driver dengan id " + driverId + " berhasil dihapus.");
            }
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", "Gagal menghapus driver: " + e.getMessage());
        }
        return "redirect:/drivers";
    }
}