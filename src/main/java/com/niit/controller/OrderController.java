package com.niit.controller;

import com.niit.entity.Appointment;
import com.niit.entity.Teacher;
import com.niit.entity.User;
import com.niit.repository.AppointmentRepository;
import com.niit.repository.TeacherRepository;
import com.niit.repository.OrderRepository;
import com.niit.entity.Order;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.niit.repository.CourseRepository;
import com.niit.entity.Course;
import java.math.BigDecimal;
import java.time.Duration;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/orders/create")
    public String createOrder(@RequestParam Integer appointmentId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null || user == null) return "redirect:/appointments/student";

        // 查找课程（假设同一学生、老师、科目、时间唯一）
        List<Course> courses = courseRepository.findByStudentId(appointment.getStudentId());
        Course course = null;
        for (Course c : courses) {
            if (c.getTeacher().getId().equals(appointment.getTeacherId()) &&
                    c.getSubject().equals(appointment.getSubject()) &&
                    c.getStartTime().equals(appointment.getStartTime())) {
                course = c;
                break;
            }
        }
        // 如果没有找到课程，自动创建一条
        if (course == null) {
            Teacher teacherEntity = teacherRepository.findByUserId(appointment.getTeacherId());
            User teacherUser = teacherEntity != null ? teacherEntity.getUser() : null;
            User studentUser = user;
            course = new Course();
            course.setTeacher(teacherUser);
            course.setStudent(studentUser);
            course.setSubject(appointment.getSubject());
            course.setStartTime(appointment.getStartTime());
            course.setEndTime(appointment.getEndTime());
            course.setAddress("");
            course.setOnline(false);
            BigDecimal price = teacherEntity != null ? new BigDecimal(teacherEntity.getPrice()) : BigDecimal.ZERO;
            course.setPrice(price);
            course.setType(Course.CourseType.一对一);
            course.setStatus(Course.CourseStatus.已预约);
            course = courseRepository.save(course);
        }
        // 计算预约时长（小时，保留两位小数）
        Duration duration = Duration.between(appointment.getStartTime(), appointment.getEndTime());
        double hours = duration.toMinutes() / 60.0;
        // 获取老师单价
        Teacher teacher = teacherRepository.findByUserId(appointment.getTeacherId());
        BigDecimal pricePerHour = teacher != null ? new BigDecimal(teacher.getPrice()) : BigDecimal.ZERO;
        // 计算总金额
        BigDecimal amount = pricePerHour.multiply(BigDecimal.valueOf(hours)).setScale(2, BigDecimal.ROUND_HALF_UP);

        // 检查是否已存在该预约的订单，避免重复下单
        final Course finalCourse = course;
        List<Order> existOrders = orderRepository.findByUserId(user.getId());
        boolean alreadyOrdered = existOrders.stream().anyMatch(o -> o.getCourse() != null && finalCourse != null && o.getCourse().getId().equals(finalCourse.getId()));
        if (!alreadyOrdered) {
            Order order = new Order();
            order.setUser(user);
            order.setCourse(course);
            order.setAmount(amount);
            order.setStatus(Order.OrderStatus.待支付);
            orderRepository.save(order);
        }
        return "redirect:/orders/my?appointmentId=" + appointmentId;
    }

    @GetMapping("/orders/my")
    public String myOrders(@RequestParam(required = false) Integer appointmentId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<Order> orders = orderRepository.findByUserId(user.getId());
        model.addAttribute("orders", orders);
        // 如果带有预约id，查找该预约的教师费用
        if (appointmentId != null) {
            Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
            if (appointment != null) {
                Teacher teacher = teacherRepository.findByUserId(appointment.getTeacherId());
                if (teacher != null) {
                    model.addAttribute("teacherPrice", teacher.getPrice());
                }
            }
        }
        return "my_orders";
    }

    @GetMapping("/orders/pay")
    public String payPage(@RequestParam Integer orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order_pay";
    }

    @PostMapping("/orders/pay")
    public String payOrder(@RequestParam Integer orderId, @RequestParam String payMethod) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && (order.getStatus().name().equals("待支付") || order.getStatus().name().equals("PENDING") )) {
            order.setPayMethod(Order.PayMethod.valueOf(payMethod));
            order.setStatus(Order.OrderStatus.已支付);
            orderRepository.save(order);
        }
        return "redirect:/orders/my";
    }

} 