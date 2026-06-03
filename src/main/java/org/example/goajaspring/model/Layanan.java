package org.example.goajaspring.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Layanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama layanan tidak boleh kosong")
    @Column(unique = true)
    private String namaLayanan;

    @Positive(message = "Tarif per km harus lebih dari 0")
    private double tarifPerKm;

    public Layanan() {
    }

    public Layanan(String namaLayanan, double tarifPerKm) {
        this.namaLayanan = namaLayanan;
        this.tarifPerKm = tarifPerKm;
    }

    public Long getId() {
        return id;
    }
    public String getNamaLayanan() {
        return namaLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
    }
    public double getTarifPerKm() {
        return tarifPerKm;
    }

    public void setTarifPerKm(double tarifPerKm) {
        this.tarifPerKm = tarifPerKm;
    }
}
