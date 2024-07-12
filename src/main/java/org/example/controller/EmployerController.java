package org.example.controller;

import org.example.DTO.EmployerRegistrationDTO;
import org.example.model.Employer;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Employer> register(@RequestBody EmployerRegistrationDTO employerDTO) {
        Employer employer = userService.registerEmployer(employerDTO);
        return ResponseEntity.ok(employer);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/profile")
    public ResponseEntity<?> getEmployerProfile(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Optional<Employer> employerOpt = userService.findByUsername(username).map(user -> (Employer) user);
        if (employerOpt.isPresent()) {
            return ResponseEntity.ok(employerOpt.get());
        } else {
            return ResponseEntity.status(404).body("Profile not found");
        }
    }
}
