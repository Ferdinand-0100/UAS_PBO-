package org.example.goajaspring.repository;

import org.example.goajaspring.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByPlatNomor(String platNomor);
}