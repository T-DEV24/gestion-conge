package com.ismat.conge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showLandingPage(Model model) {
        return "landing"; // Retourne le template landing.html
    }
}