package com.niit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SelectController {
    @GetMapping("/select")
    public String redirectToTeacherSelect() {
        return "forward:/teacher/select";
    }
} 