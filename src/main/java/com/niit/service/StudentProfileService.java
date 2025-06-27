package com.niit.service;

import com.niit.entity.Student;

import java.util.Date;

public interface StudentProfileService {
    Student completeProfile(Integer userId, String grade, String needs);

    Student completeProfile(Integer userId, String realName, String gender, String idCard,
                            String nativePlaceName, Date birthday, String grade, String needs);

    Student getProfile(Integer userId);
}