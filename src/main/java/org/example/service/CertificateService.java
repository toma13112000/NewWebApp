package org.example.service;

import org.example.exception.CertificateNotFoundException;
import org.example.model.CertificateRegistry;
import org.example.model.Graduate;
import org.example.repository.CertificateRegistryRepository;
import org.example.repository.GraduateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CertificateService {

    @Autowired
    private CertificateRegistryRepository certificateRegistryRepository;

    @Autowired
    private GraduateRepository graduateRepository;

    @Transactional
    public CertificateRegistry addCertificate(CertificateRegistry certificate) {
        Graduate graduate = certificate.getGraduate();
        if (graduate.getId() == null) {
            graduate = graduateRepository.save(graduate);
            certificate.setGraduate(graduate); // Обновление объекта CertificateRegistry для ссылки на сохраненный объект Graduate
        }
        return certificateRegistryRepository.save(certificate);
    }

    @Transactional
    public void removeCertificate(Long certificateId) {
        if (!certificateRegistryRepository.existsById(certificateId)) {
            throw new CertificateNotFoundException("Certificate not found");
        }
        certificateRegistryRepository.deleteById(certificateId);
    }

    @Transactional
    public void updateCertificate(CertificateRegistry updatedCertificate) {
        CertificateRegistry existingCertificate = certificateRegistryRepository.findById(updatedCertificate.getId())
                .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));

        existingCertificate.setCertificateNumber(updatedCertificate.getCertificateNumber());
        existingCertificate.setCertificateDate(updatedCertificate.getCertificateDate());
        existingCertificate.setSpecialty(updatedCertificate.getSpecialty());

        certificateRegistryRepository.save(existingCertificate);
    }
}
