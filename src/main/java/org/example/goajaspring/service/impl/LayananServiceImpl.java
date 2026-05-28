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
        if (layanan.getNamaLayanan() != null && layananRepository.existsByNamaLayanan(layanan.getNamaLayanan())) {
            throw new RuntimeException("Layanan dengan nama yang sama sudah ada");
        }
        return layananRepository.save(layanan);
    }

    @Override
    public List<Layanan> getAllLayanan() {
        return layananRepository.findAll();
    }
}

