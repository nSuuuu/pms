package com.niit.service;

import com.niit.dto.TeacherInfoDTO;
import com.niit.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TeacherService {
    public List<TeacherInfoDTO> getTeachersWithFilters(
            String gradeLevel,
            String subject,
            String province,
            String city,
            Integer minPrice,
            Integer maxPrice);

    public List<TeacherInfoDTO> getAllTeachers();
}