package com.niit.controller;

import com.niit.entity.Course;
import com.niit.entity.Teacher;
import com.niit.entity.User;
import com.niit.repository.CourseRepository;
import com.niit.service.AuthService;
import com.niit.service.TeacherProfileService;
import com.niit.service.TeacherService;
import com.niit.utils.IdCardValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private AuthService authService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherProfileService teacherProfileService;

    @GetMapping("/profile")
    public String teacherProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }

        Teacher teacher = teacherProfileService.getProfile(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("teacher", teacher);
        return "teacher_profile";
    }

    @PostMapping("/profile")
    @ResponseBody
    public String updateTeacherProfile(
            @RequestParam String realName,
            @RequestParam String gender,
            @RequestParam String idCard,
            @RequestParam String province,
            @RequestParam String city,
            @RequestParam String subjects,
            @RequestParam String education,
            @RequestParam String style,
            @RequestParam String experience,
            @RequestParam String gradeLevel,
            @RequestParam Integer price,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }

        // ID card validation
        if (!IdCardValidator.isValid(idCard)) {
            return "身份证号格式不正确";
        }

        // Check if ID card is being changed
        if (!idCard.equals(user.getIdCard())) {
            if (authService.isIdCardExists(idCard, user.getId())) {
                return "该身份证号已被其他用户使用";
            }
        }

        // Update user basic info
        authService.updateUserProfile(user.getId(), realName, gender, idCard, province, city);

        // Update teacher specific info
        teacherProfileService.completeProfile(
                user.getId(),
                subjects,
                education,
                style,
                experience,
                gradeLevel,
                price
        );

        return "success";
    }

    @PostMapping("/avatar")
    public String updateAvatar(@RequestParam("avatar") MultipartFile avatarFile,
                               HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }

        try {
            Teacher teacher = teacherProfileService.updateAvatar(user.getId(), avatarFile);
            model.addAttribute("success", "头像上传成功");
            model.addAttribute("teacher", teacher);
        } catch (Exception e) {
            model.addAttribute("error", "头像上传失败: " + e.getMessage());
        }

        model.addAttribute("user", user);
        return "teacher_profile";
    }

    @GetMapping("/center")
    public String teacherCenter(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }

        Teacher teacher = teacherProfileService.getProfile(user.getId());
        List<Course> courses = courseRepository.findByTeacherId(user.getId());
        long studentCount = courses.stream().map(c -> c.getStudent().getId()).distinct().count();

        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courses);
        model.addAttribute("courseCount", courses.size());
        model.addAttribute("studentCount", studentCount);
        return "teacher_center";
    }
}