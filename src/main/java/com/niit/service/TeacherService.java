package com.niit.service;

import com.niit.dto.TeacherInfoDTO;
import com.niit.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public List<TeacherInfoDTO> getTeachersWithFilters(
            String gradeLevel,
            String subject,
            String province,
            String city,
            Integer minPrice,
            Integer maxPrice) {
        // 调用 JPA Repository 方法
        return teacherRepository.findTeachersWithFilters(
                gradeLevel,
                subject != null ? "%" + subject + "%" : null, // 处理 LIKE 查询
                province,
                city,
                minPrice,
                maxPrice
        );
    }

    public List<TeacherInfoDTO> getAllTeachers() {
        // 调用 JPA Repository 方法，传递 null 参数表示不限制条件
        return teacherRepository.findTeachersWithFilters(null, null, null, null, null, null);
    }
}