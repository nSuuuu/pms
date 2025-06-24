package com.niit.service;

import com.niit.dto.TeacherInfoDTO;
import com.niit.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    public List<TeacherInfoDTO> getTeachersWithFilters(
            String gradeLevel,
            String subject,
            String province,
            String city,
            Integer minPrice,
            Integer maxPrice) {
        return teacherMapper.findTeachersWithFilters(gradeLevel, subject, province, city, minPrice, maxPrice);
    }

    public List<TeacherInfoDTO> getAllTeachers() {
        return teacherMapper.findTeachersWithFilters(null, null, null, null, null, null);
    }
} 