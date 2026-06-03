package org.example.goajaspring.controller;

import org.example.goajaspring.model.Driver;
import org.example.goajaspring.model.DriverApplication;
import org.example.goajaspring.service.DriverService;
import org.example.goajaspring.service.LayananService;
import org.example.goajaspring.repository.DriverApplicationRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DriverApplicationController {

    private final DriverApplicationRepository appRepo;
    private final LayananService layananService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;

    public DriverApplicationController(DriverApplicationRepository appRepo,
                                       LayananService layananService,
                                       DriverService driverService,
                                       PasswordEncoder passwordEncoder) {
        this.appRepo = appRepo;
        this.layananService = layananService;
        this.driverService = driverService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/driver/apply")
    public String applyForm(Model model, Authentication authentication) {
        model.addAttribute("application", new DriverApplication());
        model.addAttribute("layananList", layananService.getAllLayanan());
        model.addAttribute("currentRole", resolveRole(authentication));
        return "driver_apply";
    }

    @PostMapping("/driver/submit")
    public String submitApplication(@Valid @ModelAttribute("application") DriverApplication application,
                                    BindingResult bindingResult,
                                    @RequestParam("photo") MultipartFile photo,
                                    @RequestParam("ktp")   MultipartFile ktp,
                                    @RequestParam("sim")   MultipartFile sim,
                                    @RequestParam("stnk")  MultipartFile stnk,
                                    Model model,
                                    Authentication authentication) {
        // File-presence validation (files can't be checked by Bean Validation)
        if (photo.isEmpty()) bindingResult.rejectValue("photoPath", "required", "Foto diri wajib diunggah");
        if (ktp.isEmpty())   bindingResult.rejectValue("ktpPath",   "required", "Scan KTP wajib diunggah");
        if (sim.isEmpty())   bindingResult.rejectValue("simPath",   "required", "Scan SIM wajib diunggah");
        if (stnk.isEmpty())  bindingResult.rejectValue("stnkPath",  "required", "Scan STNK wajib diunggah");

        if (bindingResult.hasErrors()) {
            model.addAttribute("layananList", layananService.getAllLayanan());
            model.addAttribute("currentRole", resolveRole(authentication));
            return "driver_apply";
        }

        try {
            application.setCreatedAt(LocalDateTime.now());
            application.setStatus("PENDING");

            application.setPhotoPath(photo.getOriginalFilename());
            application.setPhotoData(photo.getBytes());
            application.setPhotoContentType(photo.getContentType());

            application.setKtpPath(ktp.getOriginalFilename());
            application.setKtpData(ktp.getBytes());
            application.setKtpContentType(ktp.getContentType());

            application.setSimPath(sim.getOriginalFilename());
            application.setSimData(sim.getBytes());
            application.setSimContentType(sim.getContentType());

            application.setStnkPath(stnk.getOriginalFilename());
            application.setStnkData(stnk.getBytes());
            application.setStnkContentType(stnk.getContentType());

            appRepo.save(application);
            model.addAttribute("success", "Aplikasi terkirim. Tunggu konfirmasi dari admin.");
            model.addAttribute("application", new DriverApplication());
            model.addAttribute("layananList", layananService.getAllLayanan());
            model.addAttribute("currentRole", resolveRole(authentication));
            return "driver_apply";
        } catch (IOException e) {
            model.addAttribute("error", "Gagal mengirim aplikasi: " + e.getMessage());
            model.addAttribute("layananList", layananService.getAllLayanan());
            model.addAttribute("currentRole", resolveRole(authentication));
            return "driver_apply";
        }
    }

    /**
     * Serves a stored document from the database.
     * URL pattern: /driver-docs/{appId}/{type}
     * type = photo | ktp | sim | stnk
     */
    @GetMapping("/driver-docs/{appId}/{type}")
    public ResponseEntity<byte[]> serveDoc(@PathVariable Long appId,
                                           @PathVariable String type) {
        return appRepo.findById(appId).map(app -> {
            byte[] data;
            String contentType;

            switch (type) {
                case "photo" -> { data = app.getPhotoData(); contentType = app.getPhotoContentType(); }
                case "ktp"   -> { data = app.getKtpData();   contentType = app.getKtpContentType(); }
                case "sim"   -> { data = app.getSimData();   contentType = app.getSimContentType(); }
                case "stnk"  -> { data = app.getStnkData();  contentType = app.getStnkContentType(); }
                default      -> { return ResponseEntity.notFound().<byte[]>build(); }
            }

            if (data == null) return ResponseEntity.notFound().<byte[]>build();

            MediaType media = parseContentType(contentType);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + type + "\"")
                    .contentType(media)
                    .body(data);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Admin: list pending applications
    @GetMapping("/drivers/applications")
    public String listApplications(Model model, Authentication authentication) {
        List<DriverApplication> list = appRepo.findByStatus("PENDING");
        model.addAttribute("applications", list);
        model.addAttribute("currentRole", resolveRole(authentication));
        return "drivers_applications";
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
            Driver d = new Driver();
            d.setNama(app.getNama());
            d.setEmail(app.getEmail());
            if (app.getPassword() == null || app.getPassword().isBlank()) {
                throw new RuntimeException("Password driver belum diisi");
            }
            d.setPassword(passwordEncoder.encode(app.getPassword()));
            d.setKendaraan(app.getKendaraan());
            d.setPlatNomor(app.getPlatNomor());
            d.setAvailable(true);
            d.setRole("DRIVER");
            driverService.saveDriver(d);

            app.setStatus("APPROVED");
            appRepo.save(app);

            ra.addFlashAttribute("success", "Aplikasi disetujui. Driver bisa login memakai email dan password yang didaftarkan.");
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

    // ── Helpers ──────────────────────────────────────────────

    private String resolveRole(Authentication authentication) {
        if (authentication == null) return "USER";
        var authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) return "USER";
        String full = authorities.iterator().next().getAuthority();
        if (full == null) return "USER";
        return full.startsWith("ROLE_") ? full.substring(5) : full;
    }

    private MediaType parseContentType(String contentType) {
        if (contentType == null) return MediaType.APPLICATION_OCTET_STREAM;
        try {
            return MediaType.parseMediaType(contentType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
