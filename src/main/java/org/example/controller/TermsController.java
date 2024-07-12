package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TermsController {

    @GetMapping("/terms")
    public String loginPage() {
        return "terms"; // имя вашего шаблона login.html
    }
}
