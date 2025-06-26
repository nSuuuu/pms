package com.niit.controller;

import com.niit.entity.Appointment;
import com.niit.entity.User;
import com.niit.repository.AppointmentRepository;
import com.niit.repository.UserRepository;
import com.niit.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentService appointmentService;

    // 学生预约列表
    @GetMapping("/student")
    public String studentAppointments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        List<Appointment> appointments = appointmentRepository.findByStudentId(user.getId());
        // 查询所有老师信息
        Map<Integer, User> teacherMap = userRepository.findAllById(
            appointments.stream().map(Appointment::getTeacherId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getId, t -> t));
        model.addAttribute("appointments", appointments);
        model.addAttribute("teacherMap", teacherMap);
        return "appointments_student";
    }

    // 老师预约列表
    @GetMapping("/teacher")
    public String teacherAppointments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        List<Appointment> appointments = appointmentRepository.findByTeacherId(user.getId());
        // 查询所有学生信息
        Map<Integer, User> studentMap = userRepository.findAllById(
            appointments.stream().map(Appointment::getStudentId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getId, s -> s));
        model.addAttribute("appointments", appointments);
        model.addAttribute("studentMap", studentMap);
        return "appointments_teacher";
    }

    // 预约老师
    @PostMapping("/book")
    public String book(@RequestParam Integer teacherId,
                       @RequestParam String subject,
                       @RequestParam String startTime,
                       @RequestParam String endTime,
                       HttpSession session) {
        User student = (User) session.getAttribute("user");
        if (student == null) return "redirect:/login";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        appointmentService.createAppointment(
            student.getId(),
            teacherId,
            subject,
            LocalDateTime.parse(startTime, formatter),
            LocalDateTime.parse(endTime, formatter)
        );
        return "redirect:/appointments/student";
    }

    // 取消预约
    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam Integer appointmentId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Appointment appt = appointmentRepository.findById(appointmentId).orElse(null);
        if (appt != null) {
            // 学生本人取消
            if (appt.getStudentId().equals(user.getId())) {
                appt.setStatus("CANCELLED");
                appointmentRepository.save(appt);
                return "redirect:/appointments/student";
            }
            // 老师本人取消
            if (appt.getTeacherId().equals(user.getId())) {
                appt.setStatus("CANCELLED");
                appointmentRepository.save(appt);
                return "redirect:/appointments/teacher";
            }
        }
        // 默认跳转到学生预约页面
        return "redirect:/appointments/student";
    }
} 