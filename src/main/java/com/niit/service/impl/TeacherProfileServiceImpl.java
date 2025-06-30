package com.niit.service.impl;

import com.niit.entity.Teacher;
import com.niit.repository.TeacherRepository;
import com.niit.service.TeacherProfileService;
import com.niit.utils.FileUploadUtil;
import com.niit.utils.IDCardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TeacherProfileServiceImpl implements TeacherProfileService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public Teacher completeProfile(Integer userId, String subjects, String education,
                                   String style, String experience, String gradeLevel,
                                   Integer price, String nativePlaceName) {
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
        teacher.setNativePlaceName(nativePlaceName);
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
            String fileName = FileUploadUtil.saveFile(uploadDir, avatarFile);
            teacher.setAvatar("/uploads/" + fileName);
            return teacherRepository.save(teacher);
        } catch (IOException e) {
            throw new RuntimeException("头像上传失败", e);
        }
    }

    @Override
    public Teacher getProfile(Integer userId) {
        return teacherRepository.findByUserId(userId);
    }
}