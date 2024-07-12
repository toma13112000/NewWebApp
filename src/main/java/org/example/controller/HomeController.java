package org.example.controller;

import org.example.model.Graduate;
import org.example.model.Advertisement;
import org.example.service.GraduateService;
import org.example.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final GraduateService graduateService;
    private final AdvertisementService advertisementService;

    @Autowired
    public HomeController(GraduateService graduateService, AdvertisementService advertisementService) {
        this.graduateService = graduateService;
        this.advertisementService = advertisementService;
    }

    @GetMapping("/index")
    public String home(Model model) {
        List<Graduate> graduates = graduateService.findAll();
        List<Advertisement> advertisements = advertisementService.findAll();

        model.addAttribute("graduates", graduates);
        model.addAttribute("advertisements", advertisements);

        return "index";
    }
}
