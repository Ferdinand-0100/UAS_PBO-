package org.example.goajaspring.service;

import org.example.goajaspring.model.Layanan;

import java.util.List;

public interface LayananService {
    Layanan saveLayanan(Layanan layanan);
    List<Layanan> getAllLayanan();
}
