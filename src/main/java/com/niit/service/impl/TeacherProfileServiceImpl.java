package com.niit.service.impl;

import com.niit.entity.Teacher;
import com.niit.repository.TeacherRepository;
import com.niit.service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherProfileServiceImpl implements TeacherProfileService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public Teacher completeProfile(Integer userId, String avatar, String subjects, String education, String style, Float score) {
        Teacher teacher = teacherRepository.findByUserId(userId);
        if (teacher == null) {
            teacher = new Teacher();
            teacher.setUserId(userId);
        }
        teacher.setAvatar(avatar);
        teacher.setSubjects(subjects);
        teacher.setEducation(education);
        teacher.setStyle(style);
        teacher.setScore(score);
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher completeProfile(Integer userId, String realName, String gender, String idCard, String province, String city, String avatarUrl, String subjects, String education, String style) {
        return null;
    }

    @Override
    public Teacher getProfile(Integer userId) {
        return teacherRepository.findByUserId(userId);
    }
} 