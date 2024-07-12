package org.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/confirm")
public class ConfirmController {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmController.class);

    @GetMapping
    public String showConfirmPage(@RequestParam String phone, HttpSession session) {
        phone = formatPhoneNumber(phone);
        logger.info("Отображение страницы подтверждения для номера: {}", phone);
        session.setAttribute("phone", phone); // Сохраняем номер телефона в сессии
        return "confirm"; // Имя HTML-шаблона для страницы подтверждения
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> confirmCode(@RequestBody Map<String, String> request, HttpSession session) {
        String code = request.get("code");
        String sessionCode = (String) session.getAttribute("verificationCode");
        String phone = (String) session.getAttribute("phone");

        phone = formatPhoneNumber(phone); // Форматируем номер телефона

        logger.info("Проверка кода подтверждения для номера: {}", phone);
        logger.info("Код в сессии: {}, Введенный код: {}", sessionCode, code);

        Map<String, Object> response = new HashMap<>();

        if (sessionCode != null && sessionCode.equals(code)) {
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Неверный код подтверждения");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    private String formatPhoneNumber(String phone) {
        if (phone != null) {
            // Удаляем все пробелы из номера телефона
            phone = phone.replaceAll("\\s+", "");

            // Добавляем "+" в начало, если его нет
            if (!phone.startsWith("+")) {
                phone = "+" + phone;
            }
        }
        return phone;
    }
}

