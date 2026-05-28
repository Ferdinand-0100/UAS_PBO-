package org.example.goajaspring.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Driver extends User{
    private String kendaraan;
    @Column(unique = true)
    private String platNomor;
    private Boolean available;

    public Driver() {
    }

    public Driver(String kendaraan, String platNomor, boolean available) {
        this.kendaraan = kendaraan;
        this.platNomor = platNomor;
        this.available = available;
    }

    public String getKendaraan() { return kendaraan;}

    public void setKendaraan(String kendaraan) {this.kendaraan = kendaraan;}

    public String getPlatNomor() { return platNomor;}

    public void setPlatNomor(String platNomor) {this.platNomor = platNomor;}

    public boolean isAvailable() {
        return available != null && available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String getInfo() {
        return "Driver: " + getNama() + " | Kendaraan: " + kendaraan + " | Plat: " + platNomor;
    }
}
