package com.niit.controller;

import com.niit.dto.TeacherInfoDTO;
import com.niit.entity.Course;
import com.niit.entity.Teacher;
import com.niit.entity.User;
import com.niit.repository.CourseRepository;
import com.niit.service.AuthService;
import com.niit.service.TeacherProfileService;
import com.niit.service.TeacherService;
import com.niit.utils.BusinessException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
        model.addAttribute("user", user);
        return "teacher_profile";
    }

    @PostMapping("/profile")
    public String updateTeacherProfile(@RequestParam String realName,
                                       @RequestParam String gender,
                                       @RequestParam String idCard,
                                       @RequestParam String province,
                                       @RequestParam String city,
                                       @RequestParam String avatar,
                                       @RequestParam String subjects,
                                       @RequestParam String education,
                                       @RequestParam String style,
                                       HttpSession session,
                                       Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }

        try {
            // 检查当前用户的身份证号是否与提交的一致，如果不一致才需要更新
            if (!user.getIdCard().equals(idCard)) {
                // 检查新身份证号是否已被其他用户使用
                if (authService.isIdCardExists(idCard, user.getId())) {
                    model.addAttribute("error", "该身份证号已被其他用户使用");
                    model.addAttribute("user", user);
                    return "teacher_profile";
                }
            }

            authService.updateTeacherProfile(user.getId(), realName, gender, idCard, province, city, avatar, subjects, education, style);
            model.addAttribute("success", "资料完善成功");
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
        }

        // 重新获取更新后的用户信息
        user = authService.getUserById(user.getId());
        session.setAttribute("user", user);
        model.addAttribute("user", user);

        return "teacher_center";
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
