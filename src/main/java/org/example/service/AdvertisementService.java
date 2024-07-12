package org.example.service;

import org.example.model.Advertisement;
import org.example.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }
    public List<Advertisement> findByTitleContaining(String title) {
        return advertisementRepository.findByTitleContaining(title);
    }

    public List<Advertisement> findByType(String type) {
        return advertisementRepository.findByType(type);
    }
    // Создание и обновление объявления
    public Advertisement save(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }

    public Optional<Advertisement> findById(Long id) {
        return advertisementRepository.findById(id);
    }

    public void deleteById(Long id) {
        advertisementRepository.deleteById(id);
    }
}
