package com.niit.service.impl;

import com.niit.entity.Appointment;
import com.niit.repository.AppointmentRepository;
import com.niit.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Appointment createAppointment(Integer studentId, Integer teacherId, String subject, LocalDateTime startTime, LocalDateTime endTime) {
        Appointment appt = new Appointment();
        appt.setStudentId(studentId);
        appt.setTeacherId(teacherId);
        appt.setSubject(subject);
        appt.setStartTime(startTime);
        appt.setEndTime(endTime);
        appt.setStatus("PENDING");
        appt.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return appointmentRepository.save(appt);
    }

    @Override
    public List<Appointment> getAppointmentsByStudent(Integer studentId) {
        return appointmentRepository.findByStudentId(studentId);
    }

    @Override
    public List<Appointment> getAppointmentsByTeacher(Integer teacherId) {
        return appointmentRepository.findByTeacherId(teacherId);
    }
} 