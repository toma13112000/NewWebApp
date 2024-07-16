package org.example.controller;

import org.example.DTO.ChangePasswordDTO;
import org.example.DTO.EmployerRegistrationDTO;
import org.example.DTO.GraduateRegistrationDTO;
import org.example.model.*;
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
import java.io.IOException;
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
                response.put("photoFile", graduate.getPhotoFile());
                response.put("portfolio", graduate.getPortfolioFiles().stream()
                        .map(PortfolioFile::getFileName)
                        .collect(Collectors.toList()));

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

                response.put("jobType", graduate.getJobType());
                response.put("employmentStatus", graduate.getEmploymentStatus());
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

    @GetMapping("/recruiting")
    public ResponseEntity<List<RecruitingCompany>> getRecruitingCompany() {
        List<RecruitingCompany> recruitingCompanies = userService.getRecruitingCompany();
        return ResponseEntity.ok(recruitingCompanies);
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
