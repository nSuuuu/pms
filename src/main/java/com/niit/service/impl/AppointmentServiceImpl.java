package com.niit.service.impl;

import com.niit.entity.Appointment;
import com.niit.entity.User;
import com.niit.repository.AppointmentRepository;
import com.niit.repository.UserRepository;
import com.niit.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Appointment bookAppointment(Integer studentId, Integer teacherId, String subject, LocalDateTime startTime, LocalDateTime endTime) {
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("学生不存在"));
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("老师不存在"));
        Appointment appointment = new Appointment();
        appointment.setStudent(student);
        appointment.setTeacher(teacher);
        appointment.setSubject(subject);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(Appointment.Status.PENDING);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancelAppointment(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            appointment.setStatus(Appointment.Status.CANCELLED);
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    @Override
    public List<Appointment> getAppointmentsByStudent(Integer studentId) {
        return appointmentRepository.findByStudentIdOrderByStartTimeDesc(studentId);
    }

    @Override
    public List<Appointment> getAppointmentsByTeacher(Integer teacherId) {
        return appointmentRepository.findByTeacherIdOrderByStartTimeDesc(teacherId);
    }
} 