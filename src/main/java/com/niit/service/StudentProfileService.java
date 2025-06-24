package com.niit.service;

import com.niit.entity.Student;

public interface StudentProfileService {
    Student completeProfile(Integer userId, String grade, String needs);

    Student completeProfile(Integer userId, String realName, String gender, String idCard, String province, String city, String grade, String needs, String avatarUrl);
    Student getProfile(Integer userId);
} 