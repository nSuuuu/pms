package com.niit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_method", columnDefinition = "ENUM('微信', '支付宝', '银行卡', '现金', '其他') DEFAULT '其他'")
    private PayMethod payMethod;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('待支付', '已支付', '退款中', '已取消', '已完成') DEFAULT '待支付'")
    private OrderStatus status;

    @Column(name = "pay_time")
    private LocalDateTime payTime;

    @Column(columnDefinition = "TEXT")
    private String refundReason;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public enum PayMethod {
        微信, 支付宝, 银行卡, 现金, 其他
    }

    public enum OrderStatus {
        待支付, 已支付, 退款中, 已取消, 已完成
    }
}