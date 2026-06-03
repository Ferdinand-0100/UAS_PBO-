package org.example.goajaspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class DriverApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama tidak boleh kosong")
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;

    @NotBlank(message = "Nomor telepon tidak boleh kosong")
    private String phone;

    @NotBlank(message = "Alamat tidak boleh kosong")
    private String alamat;

    @NotBlank(message = "Jenis kendaraan tidak boleh kosong")
    private String kendaraan;

    @NotBlank(message = "Plat nomor tidak boleh kosong")
    private String platNomor;

    @NotNull(message = "Pilih jenis layanan")
    private Long layananId;

    // Original filename (for display only)
    private String photoPath;
    private String ktpPath;
    private String simPath;
    private String stnkPath;

    // File bytes stored in DB
    @Column(columnDefinition = "bytea") private byte[] photoData;
    @Column(columnDefinition = "bytea") private byte[] ktpData;
    @Column(columnDefinition = "bytea") private byte[] simData;
    @Column(columnDefinition = "bytea") private byte[] stnkData;

    // MIME types for serving
    private String photoContentType;
    private String ktpContentType;
    private String simContentType;
    private String stnkContentType;

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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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

    public byte[] getPhotoData() { return photoData; }
    public void setPhotoData(byte[] photoData) { this.photoData = photoData; }

    public byte[] getKtpData() { return ktpData; }
    public void setKtpData(byte[] ktpData) { this.ktpData = ktpData; }

    public byte[] getSimData() { return simData; }
    public void setSimData(byte[] simData) { this.simData = simData; }

    public byte[] getStnkData() { return stnkData; }
    public void setStnkData(byte[] stnkData) { this.stnkData = stnkData; }

    public String getPhotoContentType() { return photoContentType; }
    public void setPhotoContentType(String photoContentType) { this.photoContentType = photoContentType; }

    public String getKtpContentType() { return ktpContentType; }
    public void setKtpContentType(String ktpContentType) { this.ktpContentType = ktpContentType; }

    public String getSimContentType() { return simContentType; }
    public void setSimContentType(String simContentType) { this.simContentType = simContentType; }

    public String getStnkContentType() { return stnkContentType; }
    public void setStnkContentType(String stnkContentType) { this.stnkContentType = stnkContentType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
