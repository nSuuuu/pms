package com.niit.controller;

import com.niit.entity.Appointment;
import com.niit.entity.User;
import com.niit.entity.Teacher;
import com.niit.entity.Student;
import com.niit.repository.AppointmentRepository;
import com.niit.repository.UserRepository;
import com.niit.service.CourseService;
import com.niit.service.TeacherService;
import com.niit.service.StudentProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentProfileService studentProfileService;

    @GetMapping("/schedule")
    public String getSchedule(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Appointment> appointments;
        if (user.getRole() == 1) { // 老师
            appointments = appointmentRepository.findByTeacherId(user.getId());
        } else if (user.getRole() == 2) { // 学生
            appointments = appointmentRepository.findByStudentId(user.getId());
        } else {
            appointments = null;
        }

        // 提取所有预约的开始日期（yyyy-MM-dd）
        List<String> courseDates = new ArrayList<>();
        if (appointments != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Appointment a : appointments) {
                if (a.getStartTime() != null) {
                    courseDates.add(a.getStartTime().format(formatter));
                }
            }
        }
        model.addAttribute("appointments", appointments);
        model.addAttribute("courseDates", courseDates); // 传递给前端
        return "schedule";
    }

    @GetMapping("/schedule_teacher")
    public String getTeacherSchedule(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return "redirect:/login";
        }
        Teacher teacher = teacherService.findByUserId(user.getId());
        model.addAttribute("teacher", teacher);
        List<Appointment> appointments = appointmentRepository.findByTeacherId(user.getId());
        // 组装studentName
        for (Appointment appt : appointments) {
            User studentUser = userRepository.findById(appt.getStudentId()).orElse(null);
            appt.setStudentName(studentUser != null ? studentUser.getRealName() : "");
        }
        // 课程日期
        List<String> courseDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Appointment a : appointments) {
            if (a.getStartTime() != null) {
                courseDates.add(a.getStartTime().format(formatter));
            }
        }
        model.addAttribute("appointments", appointments);
        model.addAttribute("courseDates", courseDates);
        return "schedule_teacher";
    }
}