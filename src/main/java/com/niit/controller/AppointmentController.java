package com.niit.controller;

import com.niit.entity.Appointment;
import com.niit.entity.User;
import com.niit.repository.AppointmentRepository;
import com.niit.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AppointmentController {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/appointments/student")
    public String studentAppointments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 2) return "redirect:/login";
        List<Appointment> appointments = appointmentRepository.findByStudentIdOrderByStartTimeDesc(user.getId());
        model.addAttribute("appointments", appointments);
        return "appointments_student";
    }

    @GetMapping("/appointments/teacher")
    public String teacherAppointments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) return "redirect:/login";
        List<Appointment> appointments = appointmentRepository.findByTeacherIdOrderByStartTimeDesc(user.getId());
        model.addAttribute("appointments", appointments);
        return "appointments_teacher";
    }

    @PostMapping("/appointments/book")
    public String bookAppointment(@RequestParam Integer teacherId,
                                  @RequestParam String subject,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime,
                                  HttpSession session) {
        User student = (User) session.getAttribute("user");
        if (student == null || student.getRole() != 2) return "redirect:/login";
        User teacher = userRepository.findById(teacherId).orElse(null);
        if (teacher == null || teacher.getRole() != 1) return "redirect:/appointments/student";
        Appointment appt = new Appointment();
        appt.setStudent(student);
        appt.setTeacher(teacher);
        appt.setSubject(subject);
        appt.setStartTime(startTime);
        appt.setEndTime(endTime);
        appt.setStatus(Appointment.Status.PENDING);
        appointmentRepository.save(appt);
        return "redirect:/appointments/student";
    }

    @PostMapping("/appointments/cancel")
    public String cancelAppointment(@RequestParam Integer appointmentId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Appointment appt = appointmentRepository.findById(appointmentId).orElse(null);
        if (appt != null && (appt.getStudent().getId().equals(user.getId()) || appt.getTeacher().getId().equals(user.getId()))) {
            appt.setStatus(Appointment.Status.CANCELLED);
            appointmentRepository.save(appt);
        }
        if (user.getRole() == 2) return "redirect:/appointments/student";
        else if (user.getRole() == 1) return "redirect:/appointments/teacher";
        else return "redirect:/";
    }
} 