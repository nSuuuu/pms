package com.niit.service.impl;

import com.niit.dto.TeacherInfoDTO;
import com.niit.repository.TeacherRepository;
import com.niit.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public List<TeacherInfoDTO> getTeachersWithFilters(
            String gradeLevel, String subject, String province, String city,
            Integer minPrice, Integer maxPrice) {

        // 统一处理空字符串为 null，确保JPQL的 IS NULL 条件能够生效
        if (gradeLevel != null && gradeLevel.trim().isEmpty()) {
            gradeLevel = null;
        }
        if (subject != null && subject.trim().isEmpty()) {
            subject = null;
        }
        if (province != null && province.trim().isEmpty()) {
            province = null;
        }
        if (city != null && city.trim().isEmpty()) {
            city = null;
        }

        // 可以添加日志来确认转换后的参数值
        System.out.println("Service层处理后参数: gradeLevel=" + gradeLevel + 
                           ", subject=" + subject + ", province=" + province + 
                           ", city=" + city + ", minPrice=" + minPrice + 
                           ", maxPrice=" + maxPrice);

        return teacherRepository.findTeachersWithFilters(
                gradeLevel, subject, province, city, minPrice, maxPrice
        );
    }

    @Override
    public List<TeacherInfoDTO> getAllTeachers() {
        // 调用 findTeachersWithFilters 方法，传入所有 null 参数以获取所有老师
        return getTeachersWithFilters(null, null, null, null, null, null);
    }
} 