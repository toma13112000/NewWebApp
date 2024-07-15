package org.example.service;

import org.example.DTO.EmployerRegistrationDTO;
import org.example.enumerate.RoleType;
import org.example.exception.EmailAlreadyExistsForRoleException;
import org.example.exception.PhoneNumberAlreadyExistsForRoleException;
import org.example.model.Employer;
import org.example.model.Role;
import org.example.repository.EmployerRepository;
import org.example.repository.UserRepository;
import org.example.util.PhoneNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.exception.EmailAlreadyExistsForRoleException;
import org.example.exception.PhoneNumberAlreadyExistsForRoleException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerEmployer(EmployerRegistrationDTO registrationDTO) {
        Role role = roleService.findByType(RoleType.EMPLOYER);

        String formattedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(registrationDTO.getPhoneNumber());

        if (existsByEmailAndRole(registrationDTO.getEmail(), RoleType.EMPLOYER)) {
            throw new EmailAlreadyExistsForRoleException("Email already exists for this role");
        }

        if (existsByPhoneNumberAndRole(formattedPhoneNumber, RoleType.EMPLOYER)) {
            throw new PhoneNumberAlreadyExistsForRoleException("Phone number already exists for this role");
        }

        Employer newEmployer = new Employer();
        newEmployer.setUsername(registrationDTO.getUsername());
        newEmployer.setEmail(registrationDTO.getEmail());
        newEmployer.setPhoneNumber(formattedPhoneNumber);
        newEmployer.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newEmployer.setCompanyName(registrationDTO.getCompanyName());
        newEmployer.setCompanyUrl(registrationDTO.getCompanyUrl());
        newEmployer.setCompanyActivity(registrationDTO.getCompanyActivity());
        newEmployer.addRole(role);
        newEmployer.setRoleType(RoleType.EMPLOYER); // Setting role_type

        employerRepository.save(newEmployer);
    }


    public boolean existsByEmailAndRole(String email, RoleType type) {
        return userRepository.existsByEmailAndRoles_Type(email, type);
    }

    public boolean existsByPhoneNumberAndRole(String phoneNumber, RoleType type) {
        return userRepository.existsByPhoneNumberAndRoles_Type(phoneNumber, type);
    }

    public void deleteByPhoneNumber(String phoneNumber) {
        userRepository.deleteByPhoneNumber(phoneNumber);
    }

    public Employer save(Employer employer) {
        return employerRepository.save(employer);
    }

    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    public Optional<Employer> findById(Long id) {
        return employerRepository.findById(id);
    }

    public void deleteById(Long id) {
        employerRepository.deleteById(id);
    }

    public List<Employer> findByCompanyName(String companyName) {
        return employerRepository.findByCompanyName(companyName);
    }
}
