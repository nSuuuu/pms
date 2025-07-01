package com.niit.controller;

import com.niit.entity.Student;
import com.niit.entity.User;
import com.niit.repository.UserRepository;
import com.niit.service.AuthService;
import com.niit.service.StudentProfileService;
import com.niit.utils.IdCardValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentProfileService studentProfileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/center")
    public String studentCenter(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Student student = studentProfileService.getProfile(user.getId());
        model.addAttribute("student", student);
        model.addAttribute("user", user);
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
        model.addAttribute("user", user);
        return "student_profile";
    }

    @PostMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> updateStudentProfile(
            @RequestParam String realName,
            @RequestParam String gender,
            @RequestParam String idCard,
            @RequestParam String province,
            @RequestParam String city,
            @RequestParam String birthday,
            @RequestParam String grade,
            @RequestParam String needs,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 身份证校验
            if (!IdCardValidator.isValid(idCard)) {
                return ResponseEntity.badRequest().body("\"身份证号码不合法\"");
            }

            String idCardBirthday = IdCardValidator.getFormattedBirthday(idCard);
            if (!idCardBirthday.equals(birthday)) {
                return ResponseEntity.badRequest().body("\"身份证出生日期与填写的出生日期不一致\"");
            }

            // 提取性别并验证是否一致
            String idCardGender = IdCardValidator.getGenderFromIdCard(idCard);
            if (!idCardGender.equalsIgnoreCase(gender)) {
                return ResponseEntity.badRequest().body("\"身份证性别与选择性别不符\"");
            }

            // 使用 AuthService 更新用户和学生信息
            authService.updateStudentProfile(user.getId(), realName, gender, idCard, province, city, grade, needs);

            // 更新 session 中的用户和学生信息
            user = userRepository.findById(user.getId()).orElseThrow();
            Student student = studentProfileService.getProfile(user.getId());
            session.setAttribute("user", user);
            session.setAttribute("student", student);

            return ResponseEntity.ok().body("success");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("\"保存失败：" + e.getMessage() + "\"");
        }
    }
}