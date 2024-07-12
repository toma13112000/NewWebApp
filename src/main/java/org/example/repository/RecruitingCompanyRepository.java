package org.example.repository;

import org.example.enumerate.RoleType;
import org.example.model.RecruitingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitingCompanyRepository extends JpaRepository<RecruitingCompany, Long> {
    boolean existsByEmailAndRoleType(String email, RoleType roleType);
    boolean existsByPhoneNumberAndRoleType(String phoneNumber, RoleType roleType);
}
