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
    private Double lokasiJemputLat;
    private Double lokasiJemputLng;
    private Double lokasiTujuanLat;
    private Double lokasiTujuanLng;
    private double jarak;
    private double totalHarga;
    private String status;

    @ManyToOne
    private User user;

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private Layanan layanan;

    private Double driverLat;
    private Double driverLng;

    private Integer estimatedArrivalMinutes;

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

    public Double getLokasiJemputLat() {
        return lokasiJemputLat;
    }

    public void setLokasiJemputLat(Double lokasiJemputLat) {
        this.lokasiJemputLat = lokasiJemputLat;
    }

    public Double getLokasiJemputLng() {
        return lokasiJemputLng;
    }

    public void setLokasiJemputLng(Double lokasiJemputLng) {
        this.lokasiJemputLng = lokasiJemputLng;
    }

    public Double getLokasiTujuanLat() {
        return lokasiTujuanLat;
    }

    public void setLokasiTujuanLat(Double lokasiTujuanLat) {
        this.lokasiTujuanLat = lokasiTujuanLat;
    }

    public Double getLokasiTujuanLng() {
        return lokasiTujuanLng;
    }

    public void setLokasiTujuanLng(Double lokasiTujuanLng) {
        this.lokasiTujuanLng = lokasiTujuanLng;
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

    public Double getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(Double driverLat) {
        this.driverLat = driverLat;
    }

    public Double getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(Double driverLng) {
        this.driverLng = driverLng;
    }

    public Integer getEstimatedArrivalMinutes() {
        return estimatedArrivalMinutes;
    }

    public void setEstimatedArrivalMinutes(Integer estimatedArrivalMinutes) {
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
    }
}
