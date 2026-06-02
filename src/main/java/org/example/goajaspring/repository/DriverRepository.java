package org.example.goajaspring.repository;

import org.example.goajaspring.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByPlatNomor(String platNomor);
    Optional<Driver> findByEmail(String email);
}