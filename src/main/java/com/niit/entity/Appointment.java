package com.niit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer studentId;
    private Integer teacherId;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // 枚举类型，PENDING/CONFIRMED/CANCELLED
    private Timestamp createTime;


} 