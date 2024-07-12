package org.example.repository;

import org.example.model.CertificateRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRegistryRepository extends JpaRepository<CertificateRegistry, Long> {
    List<CertificateRegistry> findBySpecialtyContaining(String specialty);
}
