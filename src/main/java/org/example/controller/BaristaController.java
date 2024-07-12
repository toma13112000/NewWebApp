package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaristaController {

    @GetMapping("/barista")
    public String loginPage() {
        return "barista"; // имя вашего шаблона login.html
    }
}
