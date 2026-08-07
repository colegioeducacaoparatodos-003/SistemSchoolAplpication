package com.SistemSchool.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebConfig {

    @GetMapping("/")
    public String home() {
        return "redirect:/index.xhtml";
    }
}