package com.niit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 255)
    private String avatar;

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String subjects;

    @Column(length = 100)
    private String education;

    @Column(columnDefinition = "TEXT")
    private String style;

    @Column(columnDefinition = "FLOAT DEFAULT 0.0")
    private Float score;

    @Column(name = "grade_level", length = 20)
    private String gradeLevel; // 小学/初中/高中

    @Column(name = "price", columnDefinition = "INT DEFAULT 0")
    private Integer price; // 每小时价格

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getSubjects() { return subjects; }
    public void setSubjects(String subjects) { this.subjects = subjects; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    public Float getScore() { return score; }
    public void setScore(Float score) { this.score = score; }

    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
}