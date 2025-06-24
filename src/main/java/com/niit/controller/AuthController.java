package com.niit.controller;

import com.niit.entity.User;
import com.niit.service.AuthService;
import com.niit.utils.BusinessException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String phone,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        try {
            User user = authService.login(phone, password);
            session.setAttribute("user", user);
            return "redirect:/";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam Integer roleType,
            @RequestParam String extraInfo,
            Model model) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);

        try {
            authService.register(user, roleType, extraInfo);
            model.addAttribute("success", "注册成功，请登录");
            return "login";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("phone", phone);
            model.addAttribute("roleType", roleType);
            model.addAttribute("extraInfo", extraInfo);
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/student/profile")
    public String studentProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 2) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "student_profile";
    }

    @PostMapping("/student/profile")
    public String updateStudentProfile(@RequestParam String realName,
                                       @RequestParam String gender,
                                       @RequestParam String idCard,
                                       @RequestParam String province,
                                       @RequestParam String city,
                                       @RequestParam String grade,
                                       @RequestParam String needs,
                                       HttpSession session,
                                       Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 2) {
            return "redirect:/login";
        }
        try {
            authService.updateStudentProfile(user.getId(), realName, gender, idCard, province, city, grade, needs);
            model.addAttribute("success", "资料完善成功");
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("user", user);
        return "student_profile";
    }

    @GetMapping("/teacher/profile")
    public String teacherProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "teacher_profile";
    }

    @PostMapping("/teacher/profile")
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
            authService.updateTeacherProfile(user.getId(), realName, gender, idCard, province, city, avatar, subjects, education, style);
            model.addAttribute("success", "资料完善成功");
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("user", user);
        return "teacher_profile";
    }
}