package org.example.controller;

import org.example.model.Advertisement;
import org.example.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping
    public ResponseEntity<Advertisement> createAdvertisement(@RequestBody Advertisement advertisement) {
        Advertisement createdAdvertisement = advertisementService.save(advertisement);
        return ResponseEntity.ok(createdAdvertisement);
    }

    @GetMapping
    public ResponseEntity<List<Advertisement>> getAllAdvertisements() {
        List<Advertisement> advertisements = advertisementService.findAll();
        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Advertisement> getAdvertisementById(@PathVariable Long id) {
        Optional<Advertisement> advertisement = advertisementService.findById(id);
        if (advertisement.isPresent()) {
            return ResponseEntity.ok(advertisement.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Дополнительные методы, если они были реализованы в сервисе
    @GetMapping("/search")
    public ResponseEntity<List<Advertisement>> searchAdvertisements(@RequestParam String title) {
        List<Advertisement> advertisements = advertisementService.findByTitleContaining(title);
        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/type")
    public ResponseEntity<List<Advertisement>> getAdvertisementsByType(@RequestParam String type) {
        List<Advertisement> advertisements = advertisementService.findByType(type);
        return ResponseEntity.ok(advertisements);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Advertisement> updateAdvertisement(@PathVariable Long id, @RequestBody Advertisement advertisementDetails) {
        Optional<Advertisement> optionalAdvertisement = advertisementService.findById(id);
        if (optionalAdvertisement.isPresent()) {
            Advertisement advertisement = optionalAdvertisement.get();
            advertisement.setTitle(advertisementDetails.getTitle());
            advertisement.setWorkSchedule(advertisementDetails.getWorkSchedule());
            advertisement.setAgeRange(advertisementDetails.getAgeRange());
            advertisement.setRequirements(advertisementDetails.getRequirements());
            advertisement.setConditions(advertisementDetails.getConditions());
            advertisement.setDescription(advertisementDetails.getDescription());
            advertisement.setType(advertisementDetails.getType());
            advertisement.setSalary(advertisementDetails.getSalary());
            Advertisement updatedAdvertisement = advertisementService.save(advertisement);
            return ResponseEntity.ok(updatedAdvertisement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
