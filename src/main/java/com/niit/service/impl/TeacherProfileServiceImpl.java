package com.niit.service.impl;

import com.niit.entity.Teacher;
import com.niit.repository.TeacherRepository;
import com.niit.service.TeacherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

@Service
public class TeacherProfileServiceImpl implements TeacherProfileService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public Teacher completeProfile(Integer userId, String subjects, String education,
                                   String style, String experience, String gradeLevel,
                                   Integer price) {
        Teacher teacher = teacherRepository.findByUserId(userId);
        if (teacher == null) {
            teacher = new Teacher();
            teacher.setUserId(userId);
        }
        teacher.setSubjects(subjects);
        teacher.setEducation(education);
        teacher.setStyle(style);
        teacher.setExperience(experience);
        teacher.setGradeLevel(gradeLevel);
        teacher.setPrice(price);
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher updateAvatar(Integer userId, MultipartFile avatarFile) {
        Teacher teacher = teacherRepository.findByUserId(userId);
        if (teacher == null) {
            teacher = new Teacher();
            teacher.setUserId(userId);
        }

        try {
            String base64Avatar = Base64.getEncoder().encodeToString(avatarFile.getBytes());
            teacher.setAvatar("data:image/png;base64," + base64Avatar);
            return teacherRepository.save(teacher);
        } catch (Exception e) {
            throw new RuntimeException("头像上传失败", e);
        }
    }

    @Override
    public Teacher getProfile(Integer userId) {
        return teacherRepository.findByUserId(userId);
    }
}