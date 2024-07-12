package org.example.controller;

import org.example.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions(HttpSession session) {
        System.out.println("Request received at /api/debug/sessions"); // Log the request
        User user = (User) session.getAttribute("user");
        if (user != null) {
            System.out.println("User found in session: " + user.getUsername()); // Log user info
            return ResponseEntity.ok().body(Map.of("username", user.getUsername(), "email", user.getEmail(), "sessionId", session.getId()));
        }
        System.out.println("No user found in session"); // Log missing user
        return ResponseEntity.status(401).body(Map.of("error", "Unauthorized", "sessionId", session.getId()));
    }
}
