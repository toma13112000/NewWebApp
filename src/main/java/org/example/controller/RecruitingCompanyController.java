package org.example.controller;

import org.example.DTO.RecruitingCompanyRegistrationDTO;
import org.example.enumerate.RoleType;
import org.example.exception.EmailAlreadyExistsForRoleException;
import org.example.exception.PhoneNumberAlreadyExistsForRoleException;
import org.example.model.Role;
import org.example.model.User;
import org.example.service.RecruitingCompanyService;
import org.example.service.RoleService;
import org.example.util.PhoneNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.example.repository.UserRepository;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/recruiting")
public class RecruitingCompanyController {

    private static final Logger logger = LoggerFactory.getLogger(RecruitingCompanyController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        String cleanedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(registrationDTO.getPhoneNumber());
        logger.info("Cleaned phone number: {}", cleanedPhoneNumber);

        try {
            registrationDTO.setPhoneNumber(cleanedPhoneNumber);
            recruitingCompanyService.registerRecruitingCompany(registrationDTO);
            logger.info("User registered successfully");
            return ResponseEntity.ok("User registered successfully");
        } catch (EmailAlreadyExistsForRoleException e) {
            logger.error("Email already exists for this role", e);
            return ResponseEntity.badRequest().body("Email already exists for this role");
        } catch (PhoneNumberAlreadyExistsForRoleException e) {
            logger.error("Phone number already exists for this role", e);
            return ResponseEntity.badRequest().body("Phone number already exists for this role");
        } catch (Exception e) {
            logger.error("Error while registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user");
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<?> checkUser(@RequestParam String phoneNumber, @RequestParam String email) {
        try {
            String cleanedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(phoneNumber);

            boolean phoneExists = recruitingCompanyService.existsByPhoneNumberAndRole(cleanedPhoneNumber, RoleType.RECRUITING_COMPANY);
            boolean emailExists = recruitingCompanyService.existsByEmailAndRole(email, RoleType.RECRUITING_COMPANY);

            return ResponseEntity.ok().body(Map.of(
                    "phoneExists", phoneExists,
                    "emailExists", emailExists));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "An error occurred"));
        }
    }

}