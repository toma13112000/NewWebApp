package org.example.controller;

import org.example.model.Graduate;
import org.example.service.GraduateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/graduate")
public class GraduateController {

    @Autowired
    private GraduateService graduateService;

    @PreAuthorize("hasRole('GRADUATE') or hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Graduate> register(@RequestBody Graduate graduate) {
        Graduate savedGraduate = graduateService.save(graduate);
        return ResponseEntity.ok(savedGraduate);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllGraduates() {
        return ResponseEntity.ok(graduateService.findAll());
    }

    @PreAuthorize("hasRole('GRADUATE')")
    @GetMapping("/profile")
    public ResponseEntity<?> getGraduateProfile(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        Optional<Graduate> graduateOpt = graduateService.findByUsername(username);
        if (graduateOpt.isPresent()) {
            return ResponseEntity.ok(graduateOpt.get());
        } else {
            return ResponseEntity.status(404).body("Profile not found");
        }
    }
}
