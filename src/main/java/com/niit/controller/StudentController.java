package com.niit.controller;

import com.niit.entity.Course;
import com.niit.entity.Student;
import com.niit.entity.User;
import com.niit.repository.CourseRepository;
import com.niit.repository.UserRepository;
import com.niit.service.AuthService;
import com.niit.service.StudentProfileService;
import com.niit.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentProfileService studentProfileService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/center")
    public String studentCenter(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 2) {
            return "redirect:/login";
        }

        Student student = studentProfileService.getProfile(user.getId());
        List<Course> courses = courseRepository.findByStudentId(user.getId());

        model.addAttribute("student", student);
        model.addAttribute("courses", courses);
        return "student_center";
    }

    @GetMapping("/profile")
    public String studentProfilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 2) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "student_profile";
    }

    @PostMapping("/profile")
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
            return "redirect:/student/center";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("user", user);
        return "student_profile";
    }
}