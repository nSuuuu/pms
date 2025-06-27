package com.niit.controller;

import com.niit.entity.Appointment;
import com.niit.entity.User;
import com.niit.entity.Teacher;
import com.niit.entity.Student;
import com.niit.entity.Order;
import com.niit.repository.AppointmentRepository;
import com.niit.repository.UserRepository;
import com.niit.service.CourseService;
import com.niit.service.TeacherService;
import com.niit.service.StudentProfileService;
import com.niit.service.OrderService;
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

    @Autowired
    private OrderService orderService;

    @GetMapping("/schedule")
    public String getSchedule(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getOrdersByUserAndStatus(user.getId(), Order.OrderStatus.已支付);
        // 提取所有订单的上课日期（假设Course有startTime字段）
        List<String> courseDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Order o : orders) {
            if (o.getCourse() != null && o.getCourse().getStartTime() != null) {
                courseDates.add(o.getCourse().getStartTime().format(formatter));
            }
        }
        model.addAttribute("orders", orders);
        model.addAttribute("courseDates", courseDates); // 传递给前端
        model.addAttribute("role", user.getRole());
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