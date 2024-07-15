package org.example.controller;

import org.example.DTO.ChangePasswordDTO;
import org.example.DTO.EmployerRegistrationDTO;
import org.example.DTO.GraduateRegistrationDTO;
import org.example.model.CertificateRegistry;
import org.example.model.Employer;
import org.example.model.Graduate;
import org.example.model.User;
import org.example.model.PortfolioFile;
import org.example.service.GraduateService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final GraduateService graduateService;

    @Autowired
    public UserController(UserService userService, GraduateService graduateService) {
        this.userService = userService;
        this.graduateService = graduateService;
    }

    @GetMapping("/graduates/specialty")
    public ResponseEntity<List<Graduate>> getGraduatesBySpecialty(@RequestParam String specialty) {
        List<Graduate> graduates = graduateService.findGraduatesBySpecialty(specialty);
        return ResponseEntity.ok(graduates);
    }

    @PostMapping("/register/graduate")
    public ResponseEntity<?> registerGraduate(
            @RequestPart("data") @Valid GraduateRegistrationDTO registrationDTO,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "portfolio", required = false) List<MultipartFile> portfolio,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        if (userService.existsByEmail(registrationDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        registrationDTO.setPhoto(photo);
        registrationDTO.setPortfolio(portfolio);

        // Convert GraduateRegistrationDTO to Graduate entity
        Graduate graduate = convertToGraduateEntity(registrationDTO);

        userService.registerGraduate(registrationDTO); // Pass registrationDTO here, not graduate

        return ResponseEntity.ok("Graduate registered successfully!");
    }

    private Graduate convertToGraduateEntity(GraduateRegistrationDTO registrationDTO) {
        Graduate graduate = new Graduate();
        graduate.setUsername(registrationDTO.getUsername());
        graduate.setEmail(registrationDTO.getEmail());
        graduate.setPhoneNumber(registrationDTO.getPhoneNumber());
        graduate.setPassword(registrationDTO.getPassword());
        graduate.setBirthDate(registrationDTO.getBirthDate());
        graduate.setJobTypes(registrationDTO.getJobTypes());
        graduate.setEmploymentStatuses(registrationDTO.getEmploymentStatuses());
        graduate.setRating(registrationDTO.getRating()); // Set rating if needed

        // Set portfolio files if provided
        List<PortfolioFile> portfolioFiles = registrationDTO.getPortfolio().stream()
                .map(file -> {
                    PortfolioFile portfolioFile = new PortfolioFile();
                    portfolioFile.setFileName(file.getOriginalFilename());
                    // Save file content to portfolioFile if needed
                    return portfolioFile;
                })
                .collect(Collectors.toList());

        graduate.setPortfolioFiles(portfolioFiles);

        // Set certificate registries if provided
        List<CertificateRegistry> certificateRegistryList = registrationDTO.getCertificates().stream()
                .map(certDTO -> new CertificateRegistry(graduate, certDTO.getCertificateNumber(), certDTO.getCertificateDate(), certDTO.getSpecialty()))
                .collect(Collectors.toList());

        graduate.setCertificateRegistryList(certificateRegistryList);

        return graduate;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        String[] parts = filename.split("\\.");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PostMapping("/register/employer")
    public ResponseEntity<?> registerEmployer(@Valid @RequestBody EmployerRegistrationDTO registrationDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        if (userService.existsByEmail(registrationDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        userService.registerEmployer(registrationDTO);
        return ResponseEntity.ok("Employer registered successfully!");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Validated @RequestBody ChangePasswordDTO request) {
        boolean isChanged = userService.changeUserPassword(request.getPhoneNumber(), request.getOldPassword(), request.getNewPassword());
        if (isChanged) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid old password");
        }
    }

    @PostMapping("/setRole")
    public ResponseEntity<?> setRole(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String role = payload.get("role");
        userService.setLastLoginRole(email, role);
        return ResponseEntity.ok("Role set successfully!");
    }

    @GetMapping("/current_user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("phoneNumber", user.getPhoneNumber());
            response.put("lastLoginRole", user.getLastLoginRole());
            response.put("role", user.getRoleType().name());

            if (user instanceof Graduate) {
                Graduate graduate = (Graduate) user;
                response.put("birthDate", graduate.getBirthDate());
                response.put("photo", graduate.getPhoto());
                response.put("photoExtension", graduate.getPhotoExtension());
                response.put("portfolio", graduate.getPortfolio());

                // Fetch CertificateRegistry details
                List<Map<String, Object>> certificates = graduate.getCertificateRegistryList().stream()
                        .map(cert -> {
                            Map<String, Object> certMap = new HashMap<>();
                            certMap.put("certificateNumber", cert.getCertificateNumber());
                            certMap.put("certificateDate", cert.getCertificateDate());
                            certMap.put("specialty", cert.getSpecialty());
                            return certMap;
                        })
                        .collect(Collectors.toList());

                response.put("certificates", certificates);

                response.put("jobTypes", graduate.getJobTypes());
                response.put("employmentStatuses", graduate.getEmploymentStatuses());
            } else if (user instanceof Employer) {
                Employer employer = (Employer) user;
                response.put("companyName", employer.getCompanyName());
                response.put("companyUrl", employer.getCompanyUrl());
                response.put("companyActivity", employer.getCompanyActivity());
            }

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(Map.of("error", "Unauthorized"));
    }


    @GetMapping("/graduates")
    public ResponseEntity<List<Graduate>> getGraduates() {
        List<Graduate> graduates = graduateService.findAll();
        log.debug("Found {} graduates", graduates.size());
        return ResponseEntity.ok(graduates);
    }

    @GetMapping("/employers")
    public ResponseEntity<List<Employer>> getEmployers() {
        List<Employer> employers = userService.getEmployers();
        return ResponseEntity.ok(employers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}