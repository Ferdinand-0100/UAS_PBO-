package org.example.goajaspring.service.impl;

import org.example.goajaspring.model.Layanan;
import org.example.goajaspring.repository.LayananRepository;
import org.example.goajaspring.service.LayananService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LayananServiceImpl implements LayananService{

    private final LayananRepository layananRepository;

    public LayananServiceImpl(LayananRepository layananRepository) {
        this.layananRepository = layananRepository;
    }

    @Override
    public Layanan saveLayanan(Layanan layanan) {
        return layananRepository.save(layanan);
    }

    @Override
    public List<Layanan> getAllLayanan() {
        return layananRepository.findAll();
    }
}

