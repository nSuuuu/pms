package com.niit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_type", columnDefinition = "ENUM('系统', '课程', '订单', '其他') DEFAULT '其他'")
    private NoticeType type;

    @Column(name = "is_read", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean read;

    @Column(name = "ref_id")
    private Integer refId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ref_type", columnDefinition = "ENUM('course', 'order', 'admin', 'other') DEFAULT 'other'")
    private RefType refType;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    public enum NoticeType {
        系统, 课程, 订单, 其他
    }

    public enum RefType {
        course, order, admin, other
    }

    // 用于系统、课程、订单等消息通知，type区分类型，refId和refType可关联业务数据
}