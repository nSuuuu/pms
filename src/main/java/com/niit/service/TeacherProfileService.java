package com.niit.service;

import com.niit.entity.Teacher;

public interface TeacherProfileService {
    Teacher completeProfile(Integer userId, String avatar, String subjects, String education, String style, Float score);

    Teacher completeProfile(Integer userId, String realName, String gender, String idCard, String province, String city, String avatarUrl, String subjects, String education, String style);
    Teacher getProfile(Integer userId);
} 