package org.example.service;

import org.example.enumerate.RoleType;
import org.example.model.RecruitingCompany;
import org.example.repository.RecruitingCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RecruitingCompanyService {

    @Autowired
    private RecruitingCompanyRepository recruitingCompanyRepository;

    public List<RecruitingCompany> findAll() {
        return recruitingCompanyRepository.findAll();
    }

    public Optional<RecruitingCompany> findById(Long id) {
        return recruitingCompanyRepository.findById(id);
    }

    @Transactional
    public RecruitingCompany save(RecruitingCompany recruitingCompany) {
        return recruitingCompanyRepository.save(recruitingCompany);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmailAndRole(String email, RoleType roleType) {
        return recruitingCompanyRepository.existsByEmailAndRoleType(email, roleType);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhoneNumberAndRole(String phoneNumber, RoleType roleType) {
        return recruitingCompanyRepository.existsByPhoneNumberAndRoleType(phoneNumber, roleType);
    }

    public void deleteById(Long id) {
        recruitingCompanyRepository.deleteById(id);
    }
}
