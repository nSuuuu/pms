package com.niit.niit.entity;

import com.niit.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private com.niit.entity.User teacher;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false, length = 100)
    private String subject;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(length = 255)
    private String address;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean online;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('一对一', '小班课', '公开课') DEFAULT '一对一'")
    private CourseType type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('待确认', '已预约', '进行中', '已完成', '已取消') DEFAULT '待确认'")
    private CourseStatus status;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public enum CourseType {
        一对一, 小班课, 公开课
    }

    public enum CourseStatus {
        待确认, 已预约, 进行中, 已完成, 已取消
    }
}