package com.niit.niit.controller;

import com.niit.dto.TeacherInfoDTO;
import com.niit.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/")
    public String home(
            @RequestParam(value = "gradeLevel", required = false) String gradeLevel,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            Model model) {

        System.out.println("gradeLevel=" + gradeLevel + ", subject=" + subject + ", province=" + province + ", city=" + city + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice);

        List<TeacherInfoDTO> teachers = teacherService.getTeachersWithFilters(
                gradeLevel, subject, province, city, minPrice, maxPrice);

        model.addAttribute("teachers", teachers);
        model.addAttribute("gradeLevel", gradeLevel);
        model.addAttribute("subject", subject);
        model.addAttribute("province", province);
        model.addAttribute("city", city);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "index";
    }
} 