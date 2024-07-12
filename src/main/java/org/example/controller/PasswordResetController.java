package org.example.controller;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/reset")
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showResetPage() {
        logger.info("Отображение страницы сброса пароля");
        return "reset"; // Имя HTML-шаблона для страницы сброса пароля
    }

    @GetMapping("/check-user")
    public ResponseEntity<Map<String, Boolean>> checkUser(@RequestParam String phoneNumber) {
        logger.info("Проверка существования пользователя для номера: {}", phoneNumber);
        try {
            phoneNumber = normalizePhoneNumber(phoneNumber);
            Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

            if (userOpt.isPresent()) {
                logger.info("Пользователь найден для номера: {}", phoneNumber);
                return ResponseEntity.ok(Map.of("exists", true));
            } else {
                logger.warn("Пользователь не найден для номера: {}", phoneNumber);
                return ResponseEntity.ok(Map.of("exists", false));
            }
        } catch (Exception e) {
            logger.error("Ошибка при проверке пользователя", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("exists", false));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PhoneRequest phoneRequest, HttpSession session) {
        logger.info("Получен запрос на сброс пароля для номера: {}", phoneRequest.getPhone());
        try {
            String phoneNumber = normalizePhoneNumber(phoneRequest.getPhone());
            logger.info("Нормализованный номер телефона: {}", phoneNumber);

            Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);
            if (userOpt.isPresent()) {
                logger.info("Пользователь найден для номера: {}", phoneNumber);
                String verificationCode = smsService.sendVerificationCode(phoneNumber);
                session.setAttribute("verificationCode", verificationCode);
                session.setAttribute("phone", phoneNumber);
                return ResponseEntity.ok(new ApiResponse(true, "Инструкции по восстановлению аккаунта были отправлены на ваш номер телефона."));
            } else {
                logger.warn("Пользователь не найден для номера телефона: {}", phoneNumber);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Пользователь не найден"));
            }
        } catch (IOException e) {
            logger.error("Ошибка при отправке СМС", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Ошибка при отправке СМС. Пожалуйста, попробуйте позже."));
        } catch (Exception e) {
            logger.error("Внутренняя ошибка сервера", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Внутренняя ошибка сервера. Пожалуйста, попробуйте позже."));
        }
    }

    @GetMapping("/confirm")
    public String showConfirmPage(@RequestParam String phone) {
        logger.info("Отображение страницы подтверждения для номера: {}", phone);
        return "confirm"; // Имя HTML-шаблона для страницы подтверждения
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse> confirmCode(@RequestBody CodeRequest codeRequest, HttpSession session) {
        String phone = (String) session.getAttribute("phone");
        String storedCode = (String) session.getAttribute("verificationCode");

        if (phone != null && storedCode != null && storedCode.equals(codeRequest.getCode())) {
            session.removeAttribute("verificationCode");
            return ResponseEntity.ok(new ApiResponse(true, "Код подтвержден"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Неверный код подтверждения"));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody PasswordRequest passwordRequest, HttpSession session) {
        String phone = (String) session.getAttribute("phone");

        if (phone != null) {
            phone = normalizePhoneNumber(phone); // Нормализация номера телефона
            Optional<User> userOpt = userRepository.findByPhoneNumber(phone);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String encodedPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
                user.setPassword(encodedPassword);
                userRepository.save(user);
                session.removeAttribute("phone");
                return ResponseEntity.ok(new ApiResponse(true, "Пароль успешно изменен"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Пользователь не найден"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Неавторизованный запрос"));
        }
    }

    private String normalizePhoneNumber(String phoneNumber) {
        return phoneNumber;
    }

    public static class PhoneRequest {
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class CodeRequest {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class PasswordRequest {
        private String newPassword;

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    public static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
