package org.example.repository;

import org.example.enumerate.RoleType;
import org.example.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    List<Employer> findByCompanyName(String companyName);
    boolean existsByEmailAndRoleType(String email, RoleType roleType);
    boolean existsByPhoneNumberAndRoleType(String phoneNumber, RoleType roleType);
}
