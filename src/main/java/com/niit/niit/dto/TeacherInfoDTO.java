package com.niit.niit.dto;

import lombok.Data;

@Data
public class TeacherInfoDTO {
    private Integer userId;
    private String realName;
    private String avatar;
    private String experience;
    private String subjects;
    private String education;
    private String style;
    private Float score;
    private String gradeLevel; // 小学/初中/高中
    private Integer price;     // 每小时价格
    private String province;
    private String city;
    private String phone;

    // 添加与 JPQL 查询参数匹配的构造函数
    public TeacherInfoDTO(
            Integer userId,
            String realName,
            String avatar,
            String experience,
            String subjects,
            String education,
            String style,
            Float score,
            String gradeLevel,
            Integer price,
            String province,
            String city,
            String phone
    ) {
        this.userId = userId;
        this.realName = realName;
        this.avatar = avatar;
        this.experience = experience;
        this.subjects = subjects;
        this.education = education;
        this.style = style;
        this.score = score;
        this.gradeLevel = gradeLevel;
        this.price = price;
        this.province = province;
        this.city = city;
        this.phone = phone;
    }

    // Getters 和 Setters 方法（完整保留）
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}