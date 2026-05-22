package org.example.goajaspring.model;
import jakarta.persistence.*;

@Entity
public class Layanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaLayanan;

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
