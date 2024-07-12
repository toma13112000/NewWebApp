package org.example.repository;

import org.example.model.Graduate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface GraduateRepository extends JpaRepository<Graduate, Long> {
    Graduate findByEmail(String email); // Если нужно искать по email
    Optional<Graduate> findByUsername(String username);
    List<Graduate> findByCertificateRegistryList_SpecialtyContaining(String specialty);
}
