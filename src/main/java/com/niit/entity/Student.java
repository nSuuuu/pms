package com.niit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 20)
    private String grade;

    @Column(columnDefinition = "TEXT")
    private String needs;

    @Column(name = "real_name", length = 50)
    private String realName;

    @Column(name = "id_card", length = 20)
    private String idCard;

    @Column(length = 10)
    private String gender;

    @Column(name = "native_place_name", length = 255)
    private String nativePlaceName;

    @Column(name = "birthday")
    private Date birthday;
}