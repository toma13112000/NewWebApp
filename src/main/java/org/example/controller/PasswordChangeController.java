package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class PasswordChangeController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/change-password")
    public String showPasswordChangePage(HttpSession session, Model model) {
        String phone = (String) session.getAttribute("phone");
        if (phone == null) {
            return "redirect:/confirm"; // Перенаправление на страницу подтверждения, если номер телефона не найден
        }

        model.addAttribute("phone", phone);
        return "change-password"; // Имя HTML-шаблона для страницы смены пароля
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("new-password") String newPassword, HttpSession session, RedirectAttributes redirectAttributes) {
        String phone = (String) session.getAttribute("phone");

        if (phone != null) {
            Optional<User> userOptional = userService.findByPhoneNumber(phone);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                userService.updateUserPasswordByPhone(phone, encodedPassword); // Использование метода из репозитория
                session.removeAttribute("phone"); // Очистка атрибута сессии
                redirectAttributes.addFlashAttribute("message", "Пароль успешно изменен.");
                return "redirect:/login"; // Перенаправление на страницу входа
            } else {
                redirectAttributes.addFlashAttribute("error", "Пользователь не найден.");
                return "redirect:/change-password"; // Перенаправление в случае ошибки
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Неавторизованный запрос.");
            return "redirect:/change-password"; // Перенаправление в случае ошибки
        }
    }
}
