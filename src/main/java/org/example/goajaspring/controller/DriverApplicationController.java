package org.example.goajaspring.controller;

import org.example.goajaspring.model.Driver;
import org.example.goajaspring.model.DriverApplication;
import org.example.goajaspring.service.DriverService;
import org.example.goajaspring.service.LayananService;
import org.example.goajaspring.repository.DriverApplicationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DriverApplicationController {

    private final DriverApplicationRepository appRepo;
    private final LayananService layananService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;

    private final Path uploadRoot = Paths.get("uploads");

    public DriverApplicationController(DriverApplicationRepository appRepo,
                                       LayananService layananService,
                                       DriverService driverService,
                                       PasswordEncoder passwordEncoder) {
        this.appRepo = appRepo;
        this.layananService = layananService;
        this.driverService = driverService;
        this.passwordEncoder = passwordEncoder;

        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException ignored) {}
    }

    @GetMapping("/driver/apply")
    public String applyForm(Model model, Authentication authentication) {
        model.addAttribute("application", new DriverApplication());
        model.addAttribute("layananList", layananService.getAllLayanan());
        // set currentRole if needed (or PageController will add)
        return "driver_apply";
    }

    @PostMapping("/driver/submit")
    public String submitApplication(@ModelAttribute DriverApplication application,
                                    @RequestParam("photo") MultipartFile photo,
                                    @RequestParam("ktp") MultipartFile ktp,
                                    @RequestParam("sim") MultipartFile sim,
                                    @RequestParam("stnk") MultipartFile stnk,
                                    RedirectAttributes ra) {
        try {
            application.setCreatedAt(LocalDateTime.now());
            application.setStatus("PENDING");

            application.setPhotoPath(storeFile(photo));
            application.setKtpPath(storeFile(ktp));
            application.setSimPath(storeFile(sim));
            application.setStnkPath(storeFile(stnk));

            appRepo.save(application);
            ra.addFlashAttribute("success", "Aplikasi terkirim. Tunggu konfirmasi dari admin.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal mengirim aplikasi: " + e.getMessage());
        }
        return "redirect:/";
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        String original = file.getOriginalFilename();
        String safeName = (original != null) ? StringUtils.cleanPath(original) : "file";
        String filename = System.currentTimeMillis() + "_" + safeName;
        Path dest = uploadRoot.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        return dest.toString();
    }

    // Admin: list pending applications
    @GetMapping("/drivers/applications")
    public String listApplications(Model model) {
        List<DriverApplication> list = appRepo.findByStatus("PENDING");

        // Extract filenames untuk display di template
        for (DriverApplication app : list) {
            if (app.getPhotoPath() != null) {
                app.setPhotoPath(getFilename(app.getPhotoPath()));
            }
            if (app.getKtpPath() != null) {
                app.setKtpPath(getFilename(app.getKtpPath()));
            }
            if (app.getSimPath() != null) {
                app.setSimPath(getFilename(app.getSimPath()));
            }
            if (app.getStnkPath() != null) {
                app.setStnkPath(getFilename(app.getStnkPath()));
            }
        }

        model.addAttribute("applications", list);
        return "drivers_applications";
    }

    private String getFilename(String path) {
        if (path == null) return null;
        int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
    }

    // Admin approve
    @PostMapping("/drivers/applications/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes ra) {
        var opt = appRepo.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("error", "Aplikasi tidak ditemukan");
            return "redirect:/drivers";
        }
        DriverApplication app = opt.get();
        try {
            // create Driver
            Driver d = new Driver();
            d.setNama(app.getNama());
            d.setEmail(app.getEmail());
            // default temporary password "123" (encode)
            d.setPassword(passwordEncoder.encode("123"));
            d.setKendaraan(app.getKendaraan());
            d.setPlatNomor(app.getPlatNomor());
            d.setAvailable(true);
            d.setRole("DRIVER");
            driverService.saveDriver(d);

            app.setStatus("APPROVED");
            appRepo.save(app);

            ra.addFlashAttribute("success", "Aplikasi disetujui. Driver dibuat dengan password sementara '123'.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Gagal approve: " + e.getMessage());
        }
        return "redirect:/drivers/applications";
    }

    @PostMapping("/drivers/applications/{id}/reject")
    public String reject(@PathVariable Long id,
                         @RequestParam(value = "rejectionReason", required = false) String reason,
                         RedirectAttributes ra) {
        var opt = appRepo.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("error", "Aplikasi tidak ditemukan");
            return "redirect:/drivers/applications";
        }
        DriverApplication app = opt.get();
        app.setStatus("REJECTED");
        if (reason != null && !reason.isBlank()) {
            app.setRejectionReason(reason);
        }
        appRepo.save(app);
        ra.addFlashAttribute("success", "Aplikasi ditolak.");
        return "redirect:/drivers/applications";
    }
}