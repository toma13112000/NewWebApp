package org.example.service;

import org.example.DTO.RecruitingCompanyRegistrationDTO;
import org.example.enumerate.RoleType;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.util.PhoneNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RecruitingCompanyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerWithRole(RecruitingCompanyRegistrationDTO registrationDTO, RoleType roleType) {
        Role role = roleService.findByType(roleType);

        User newUser = new User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setEmail(registrationDTO.getEmail());
        // Гарантируем, что номер телефона форматирован правильно перед сохранением
        newUser.setPhoneNumber(PhoneNumberUtils.formatPhoneNumber(registrationDTO.getPhoneNumber()));
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newUser.addRole(role);

        userRepository.save(newUser);
    }

    public boolean existsByEmailAndRole(String email, RoleType type) {
        return userRepository.existsByEmailAndRoles_Type(email, type);
    }

    public boolean existsByPhoneNumberAndRole(String phoneNumber, RoleType type) {
        return userRepository.existsByPhoneNumberAndRoles_Type(phoneNumber, type);
    }
}
