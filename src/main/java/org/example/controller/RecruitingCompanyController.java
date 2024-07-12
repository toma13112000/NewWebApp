package org.example.controller;

import org.example.DTO.RecruitingCompanyRegistrationDTO;
import org.example.enumerate.RoleType;
import org.example.model.RecruitingCompany;
import org.example.model.Role;
import org.example.service.RecruitingCompanyService;
import org.example.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/recruiting")
public class RecruitingCompanyController {

    private static final Logger logger = LoggerFactory.getLogger(RecruitingCompanyController.class);

    @Autowired
    private RecruitingCompanyService recruitingCompanyService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RecruitingCompanyRegistrationDTO registrationDTO, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Validation errors: {}", result.getAllErrors());
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        if (!registrationDTO.getPassword().equals(registrationDTO.getPassword2())) {
            logger.error("Passwords do not match");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }

        RecruitingCompany recruitingCompany = new RecruitingCompany();
        recruitingCompany.setUsername(registrationDTO.getUsername());
        recruitingCompany.setEmail(registrationDTO.getEmail());
        recruitingCompany.setPhoneNumber(registrationDTO.getPhoneNumber().replaceAll("\\D", ""));
        recruitingCompany.setPassword(registrationDTO.getPassword());
        recruitingCompany.setRoleType(registrationDTO.getRoleType());

        // Назначение роли RECRUITING_COMPANY
        Role role = roleService.findByType(RoleType.RECRUITING_COMPANY);
        if (role != null) {
            recruitingCompany.addRole(role);
        } else {
            logger.error("Role RECRUITING_COMPANY not found");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Role RECRUITING_COMPANY not found");
        }

        try {
            logger.info("Saving recruiting company: {}", recruitingCompany);
            RecruitingCompany savedRecruitingCompany = recruitingCompanyService.save(recruitingCompany);
            logger.info("Successfully registered new recruiting company: {}", savedRecruitingCompany);
            return ResponseEntity.ok(savedRecruitingCompany);
        } catch (Exception e) {
            logger.error("Error saving recruiting company", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving recruiting company: " + e.getMessage());
        }
    }
}
