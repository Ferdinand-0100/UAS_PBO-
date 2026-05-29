package org.example.goajaspring.service.impl;

import org.example.goajaspring.model.Driver;
import org.example.goajaspring.repository.DriverRepository;
import org.example.goajaspring.service.DriverService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService{

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver saveDriver(Driver driver) {
        if (driver.getPlatNomor() != null && driverRepository.existsByPlatNomor(driver.getPlatNomor())) {
            throw new RuntimeException("Plat nomor sudah terdaftar");
        }
        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public void deleteDriver(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new RuntimeException("Driver dengan id " + driverId + " tidak ditemukan");
        }
        driverRepository.deleteById(driverId);
    }
}
