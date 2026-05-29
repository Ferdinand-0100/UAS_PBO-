package org.example.goajaspring.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DriverApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;
    private String email;
    private String phone;
    private String alamat;

    private String kendaraan;
    private String platNomor;
    private Long layananId; // referensi ke layanan yang dipilih

    private String photoPath;
    private String ktpPath;
    private String simPath;
    private String stnkPath;

    private String status; // PENDING, APPROVED, REJECTED

    private LocalDateTime createdAt;

    private String rejectionReason; // alasan penolakan jika status = REJECTED

    public DriverApplication() {}

    // ---- getters & setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getKendaraan() { return kendaraan; }
    public void setKendaraan(String kendaraan) { this.kendaraan = kendaraan; }

    public String getPlatNomor() { return platNomor; }
    public void setPlatNomor(String platNomor) { this.platNomor = platNomor; }

    public Long getLayananId() { return layananId; }
    public void setLayananId(Long layananId) { this.layananId = layananId; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getKtpPath() { return ktpPath; }
    public void setKtpPath(String ktpPath) { this.ktpPath = ktpPath; }

    public String getSimPath() { return simPath; }
    public void setSimPath(String simPath) { this.simPath = simPath; }

    public String getStnkPath() { return stnkPath; }
    public void setStnkPath(String stnkPath) { this.stnkPath = stnkPath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}