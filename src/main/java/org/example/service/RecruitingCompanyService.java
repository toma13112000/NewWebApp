package org.example.service;

import org.example.DTO.RecruitingCompanyRegistrationDTO;
import org.example.enumerate.RoleType;
import org.example.exception.EmailAlreadyExistsForRoleException;
import org.example.exception.PhoneNumberAlreadyExistsForRoleException;
import org.example.model.Employer;
import org.example.model.RecruitingCompany;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.RecruitingCompanyRepository;
import org.example.repository.UserRepository;
import org.example.util.PhoneNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.enumerate.RoleType.RECRUITING_COMPANY;

@Service
public class RecruitingCompanyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RecruitingCompanyRepository recruitingCompanyRepository;

    @Transactional
    public void registerRecruitingCompany(RecruitingCompanyRegistrationDTO registrationDTO) {
        Role role = roleService.findByType(RoleType.RECRUITING_COMPANY);
        String formattedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(registrationDTO.getPhoneNumber());

        if (existsByEmailAndRole(registrationDTO.getEmail(), RoleType.RECRUITING_COMPANY)) {
            throw new EmailAlreadyExistsForRoleException("Email already exists for this role");
        }

        if (existsByPhoneNumberAndRole(formattedPhoneNumber, RoleType.RECRUITING_COMPANY)) {
            throw new PhoneNumberAlreadyExistsForRoleException("Phone number already exists for this role");
        }

        RecruitingCompany newRecruitingCompany = new RecruitingCompany();
        newRecruitingCompany.setUsername(registrationDTO.getUsername());
        newRecruitingCompany.setEmail(registrationDTO.getEmail());
        newRecruitingCompany.setPhoneNumber(formattedPhoneNumber);
        newRecruitingCompany.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newRecruitingCompany.addRole(role);
        newRecruitingCompany.setRoleType(RECRUITING_COMPANY); // Setting role_type

        recruitingCompanyRepository.save(newRecruitingCompany);
    }



    public boolean existsByEmailAndRole(String email, RoleType type) {
        return userRepository.existsByEmailAndRoles_Type(email, RoleType.RECRUITING_COMPANY);
    }

    public boolean existsByPhoneNumberAndRole(String phoneNumber, RoleType type) {
        return userRepository.existsByPhoneNumberAndRoles_Type(phoneNumber, RoleType.RECRUITING_COMPANY);
    }
    public void deleteByPhoneNumber(String phoneNumber) {
        userRepository.deleteByPhoneNumber(phoneNumber);
    }

}
