package com.niit.service;

import com.niit.entity.Teacher;
import org.springframework.web.multipart.MultipartFile;

public interface TeacherProfileService {

    Teacher updateAvatar(Integer userId, MultipartFile avatarFile);

    Teacher getProfile(Integer userId);

    Teacher completeProfile(Integer userId, String subjects, String education,
                                   String style, String experience, String gradeLevel,
                                   Integer price);
}