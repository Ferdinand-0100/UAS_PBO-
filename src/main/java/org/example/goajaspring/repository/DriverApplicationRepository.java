package org.example.goajaspring.repository;

import org.example.goajaspring.model.DriverApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverApplicationRepository extends JpaRepository<DriverApplication, Long> {
    List<DriverApplication> findByStatus(String status);
}