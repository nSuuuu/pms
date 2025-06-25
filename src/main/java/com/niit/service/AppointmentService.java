package com.niit.service;

import com.niit.entity.Appointment;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(Integer studentId, Integer teacherId, String subject, LocalDateTime startTime, LocalDateTime endTime);
    List<Appointment> getAppointmentsByStudent(Integer studentId);
    List<Appointment> getAppointmentsByTeacher(Integer teacherId);
} 