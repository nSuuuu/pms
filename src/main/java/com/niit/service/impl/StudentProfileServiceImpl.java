package com.niit.service.impl;

import com.niit.entity.Student;
import com.niit.repository.StudentRepository;
import com.niit.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student completeProfile(Integer userId, String grade, String needs) {
        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            student = new Student();
            student.setUserId(userId);
        }
        student.setGrade(grade);
        student.setNeeds(needs);
        return studentRepository.save(student);
    }

    @Override
    public Student completeProfile(Integer userId, String realName, String gender, String idCard, String province, String city, String grade, String needs, String avatarUrl) {
        return null;
    }

    @Override
    public Student getProfile(Integer userId) {
        return studentRepository.findByUserId(userId);
    }
} 