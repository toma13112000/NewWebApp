package org.example.controller;

import org.example.DTO.CertificateDTO;
import org.example.DTO.GraduateRegistrationDTO;
import org.example.enumerate.RoleType;
import org.example.exception.EmailAlreadyExistsForRoleException;
import org.example.exception.InvalidRequestException;
import org.example.exception.PhoneNumberAlreadyExistsForRoleException;
import org.example.model.CertificateRegistry;
import org.example.model.Employer;
import org.example.model.Graduate;
import org.example.model.Role;
import org.example.repository.GraduateRepository;
import org.example.repository.UserRepository;
import org.example.service.GraduateService;
import org.example.service.RoleService;
import org.example.util.PhoneNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/graduate")
public class GraduateController {
    private static final Logger logger = LoggerFactory.getLogger(GraduateController.class);

    @Autowired
    private GraduateService graduateService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestPart("data") @Valid GraduateRegistrationDTO registrationDTO,
            @RequestParam(value = "photoFile", required = true) MultipartFile photo,
            @RequestParam(value = "portfolioFiles", required = false) List<MultipartFile> portfolio,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data");
        }

        // Установка файлов в DTO
        registrationDTO.setPhotoFile(photo);
        registrationDTO.setPortfolioFiles(portfolio);

        try {
            Graduate newGraduate = graduateService.registerGraduate(registrationDTO, result);

            logger.info("User registered successfully");
            return ResponseEntity.ok("User registered successfully");
        } catch (EmailAlreadyExistsForRoleException | PhoneNumberAlreadyExistsForRoleException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error while registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user");
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<?> checkUser(@RequestParam String phoneNumber, @RequestParam String email) {
        try {
            Map<String, Boolean> userExistsMap = graduateService.checkUserExists(phoneNumber, email);
            return ResponseEntity.ok(userExistsMap);
        } catch (Exception ex) {
            logger.error("Error while checking user", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "An error occurred"));
        }
    }
}
