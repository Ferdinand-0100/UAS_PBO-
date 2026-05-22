package org.example.goajaspring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lokasiJemput;

    private String lokasiTujuan;

    private double jarak;

    private double totalHarga;

    private String status;

    @ManyToOne
    private User user;

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private Layanan layanan;

    public Order() {
    }

    public Order(String lokasiJemput,
                 String lokasiTujuan,
                 double jarak,
                 double totalHarga,
                 String status,
                 User user,
                 Driver driver,
                 Layanan layanan) {

        this.lokasiJemput = lokasiJemput;
        this.lokasiTujuan = lokasiTujuan;
        this.jarak = jarak;
        this.totalHarga = totalHarga;
        this.status = status;
        this.user = user;
        this.driver = driver;
        this.layanan = layanan;
    }

    public Long getId() {
        return id;
    }

    public String getLokasiJemput() {
        return lokasiJemput;
    }

    public void setLokasiJemput(String lokasiJemput) {
        this.lokasiJemput = lokasiJemput;
    }

    public String getLokasiTujuan() {
        return lokasiTujuan;
    }

    public void setLokasiTujuan(String lokasiTujuan) {
        this.lokasiTujuan = lokasiTujuan;
    }

    public double getJarak() {
        return jarak;
    }

    public void setJarak(double jarak) {
        this.jarak = jarak;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public void setLayanan(Layanan layanan) {
        this.layanan = layanan;
    }
}
