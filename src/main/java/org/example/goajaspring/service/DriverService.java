package org.example.goajaspring.service;

import org.example.goajaspring.model.Driver;

import java.util.List;

public interface DriverService {

    Driver saveDriver(Driver driver);

    List<Driver> getAllDrivers();
}
