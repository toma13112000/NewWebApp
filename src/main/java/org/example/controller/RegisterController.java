package org.example.controller;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User()); // Передаем пустой объект User для заполнения формы
        return "register"; // Имя HTML-шаблона для страницы регистрации
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        String phoneNumber = user.getPhoneNumber();
        String password = user.getPassword();

        // Ensure the phone number does not have a plus sign and starts with 7

        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }
        else {
            return phoneNumber;
        }

        // Check if the user already exists
        Optional<User> existingUserOpt = userRepository.findByPhoneNumber(phoneNumber);
        if (existingUserOpt.isPresent()) {
            model.addAttribute("message", "Пользователь с таким номером телефона уже зарегистрирован");
            return "register"; // Возвращаем страницу регистрации с сообщением об ошибке
        }

        // Create new user
        User newUser = new User();
        newUser.setPhoneNumber(phoneNumber);
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);

        model.addAttribute("message", "Пользователь успешно зарегистрирован");
        return "redirect:/login"; // Перенаправляем на страницу входа после успешной регистрации
    }
}
