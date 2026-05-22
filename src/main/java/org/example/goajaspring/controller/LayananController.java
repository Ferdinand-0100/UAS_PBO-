package org.example.goajaspring.controller;

import org.example.goajaspring.model.Layanan;
import org.example.goajaspring.service.LayananService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/layanan")
public class LayananController {

    private final LayananService layananService;

    public LayananController(LayananService layananService) {
        this.layananService = layananService;
    }

    @PostMapping
    public Layanan saveLayanan(@RequestBody Layanan layanan) {
        return layananService.saveLayanan(layanan);
    }

    @GetMapping
    public List<Layanan> getAllLayanan() {
        return layananService.getAllLayanan();
    }
}
