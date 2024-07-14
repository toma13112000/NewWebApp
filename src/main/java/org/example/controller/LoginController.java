package org.example.controller;

import org.example.authentication.JwtTokenUtil;
import org.example.model.User;
import org.example.service.CustomUserDetailsService;
import org.example.repository.UserRepository;
import org.example.util.PhoneNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid Map<String, String> loginRequest) {
        String rawPhoneNumber = loginRequest.get("phoneNumber");
        String cleanedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(rawPhoneNumber);
        String password = loginRequest.get("password");

        logger.info("Raw phone number: {}", rawPhoneNumber);
        logger.info("Cleaned phone number: {}", cleanedPhoneNumber);

        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(cleanedPhoneNumber);
            logger.info("User details found: {}", userDetails.getUsername());

            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                String token = jwtTokenUtil.generateToken(userDetails);
                logger.info("Password matches. Token generated.");
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Map.of("success", true, "token", token));
            } else {
                logger.error("Invalid password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Invalid password"));
            }
        } catch (UsernameNotFoundException ex) {
            logger.error("User not found for phone number: {}", cleanedPhoneNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "User not found"));
        } catch (Exception ex) {
            logger.error("An error occurred: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "An error occurred"));
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<?> checkUser(@RequestParam String phoneNumber) {
        try {
            String cleanedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(phoneNumber);
            logger.info("Checking user with phone number: {}", cleanedPhoneNumber);

            Optional<User> userOpt = userRepository.findByPhoneNumber(cleanedPhoneNumber);
            return userOpt.map(user -> ResponseEntity.ok(Map.of("exists", true, "user", user)))
                    .orElseGet(() -> ResponseEntity.ok(Map.of("exists", false)));
        } catch (Exception ex) {
            logger.error("An error occurred: {}", ex.getMessage());
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
            logger.error("An error occurred: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "An error occurred"));
        }
    }
}
