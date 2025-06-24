package com.niit.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(name = "real_name", nullable = false, length = 100)
    private String realName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('男', '女')")
    private Gender gender;
    @Column(name = "id_card", nullable = false, unique = true, length = 18)
    private String idCard;
    @Column(nullable = false, length = 20)
    private String phone;
    @Column(nullable = false, length = 50)
    private String province;
    @Column(nullable = false, length = 50)
    private String city;
    @Column(nullable = false)
    private Integer role; // 0=管理员, 1=老师, 2=学生
    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    public enum Gender {
        男, 女
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}