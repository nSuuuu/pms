package com.niit.controller;

import com.niit.entity.Course;
import com.niit.entity.User;
import com.niit.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/schedule")
    public String getSchedule(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Course> courses;
        if (user.getRole() == 1) { // 老师
            courses = courseService.getCoursesByTeacher(user);
        } else if (user.getRole() == 2) { // 学生
            courses = courseService.getCoursesByStudent(user);
        } else {
            // 其他角色，没有课表
            courses = null;
        }

        model.addAttribute("courses", courses);
        return "schedule";
    }
}