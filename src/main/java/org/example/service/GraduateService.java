package org.example.service;

import org.example.model.Graduate;
import org.example.model.CertificateRegistry;
import org.example.repository.CertificateRegistryRepository;
import org.example.repository.GraduateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GraduateService {

    private final GraduateRepository graduateRepository;
    private final CertificateRegistryRepository certificateRegistryRepository;

    @Autowired
    public GraduateService(GraduateRepository graduateRepository, CertificateRegistryRepository certificateRegistryRepository) {
        this.graduateRepository = graduateRepository;
        this.certificateRegistryRepository = certificateRegistryRepository;
    }

    public Graduate save(Graduate graduate) {
        return graduateRepository.save(graduate);
    }

    public List<Graduate> findAll() {
        return graduateRepository.findAll();
    }

    public Optional<Graduate> findById(Long id) {
        return graduateRepository.findById(id);
    }

    public void deleteById(Long id) {
        graduateRepository.deleteById(id);
    }

    public Optional<Graduate> findByUsername(String username) {
        return graduateRepository.findByUsername(username);
    }

    public List<Graduate> findGraduatesBySpecialty(String specialty) {
        List<CertificateRegistry> certificates = certificateRegistryRepository.findBySpecialtyContaining(specialty);
        return certificates.stream()
                .map(CertificateRegistry::getGraduate)
                .distinct()
                .collect(Collectors.toList());
    }
}
