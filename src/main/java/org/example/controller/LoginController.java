package org.example.controller;

import org.example.authentication.JwtTokenUtil;
import org.example.model.User;
import org.example.service.CustomUserDetailsService;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok()
                .contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
                .body(Map.of("success", true, "message", "API is accessible"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid Map<String, String> loginRequest) {
        String phoneNumber = loginRequest.get("phoneNumber").replaceAll("[^\\d+]", "");
        String password = loginRequest.get("password");

        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(phoneNumber);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                String token = jwtTokenUtil.generateToken(userDetails);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Map.of("success", true, "token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Invalid password"));
            }
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "User not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "An error occurred"));
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<?> checkUser(@RequestParam String phoneNumber) {
        try {
            Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber.replaceAll("[^\\d+]", ""));
            return userOpt.map(user -> ResponseEntity.ok(Map.of("exists", true, "user", user)))
                    .orElseGet(() -> ResponseEntity.ok(Map.of("exists", false)));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "An error occurred"));
        }
    }

    @GetMapping("/current_user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails user = (UserDetails) authentication.getPrincipal();
                return ResponseEntity.ok().body(Map.of("username", user.getUsername(), "email", user.getUsername()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "An error occurred"));
        }
    }
}
