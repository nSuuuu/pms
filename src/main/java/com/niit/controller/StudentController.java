package com.niit.controller;

import com.niit.entity.Student;
import com.niit.entity.User;
import com.niit.repository.StudentRepository;
import com.niit.repository.UserRepository;
import com.niit.service.StudentProfileService;
import com.niit.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/center")
    public String studentCenter(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Student student = studentProfileService.getProfile(user.getId());
        model.addAttribute("student", student);
        return "student_center";
    }

    @GetMapping("/profile")
    public String getProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Student student = studentProfileService.getProfile(user.getId());
        model.addAttribute("student", student);
        return "student_profile";
    }

    @PostMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> updateStudentProfile(
            @RequestParam String realName,
            @RequestParam String gender,
            @RequestParam String idCard,
            @RequestParam String nativePlaceName,
            @RequestParam String birthday,
            @RequestParam String grade,
            @RequestParam String needs,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = sdf.parse(birthday);

            Student student = studentProfileService.completeProfile(
                    user.getId(),
                    realName,
                    gender,
                    idCard,
                    nativePlaceName,
                    birthDate,
                    grade,
                    needs
            );

            // 更新session中的用户信息
            session.setAttribute("student", student);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}