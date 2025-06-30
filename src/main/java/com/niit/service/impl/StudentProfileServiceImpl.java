package com.niit.service.impl;

import com.niit.entity.IdCardValidator;
import com.niit.entity.NameValidator;
import com.niit.entity.Student;
import com.niit.entity.User;
import com.niit.repository.StudentRepository;
import com.niit.repository.UserRepository;
import com.niit.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
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
    @Transactional
    public Student completeProfile(Integer userId, String realName, String gender, String idCard,
                                   String nativePlaceName, Date birthday, String grade, String needs) {
        if (!NameValidator.isValidChineseName(realName)) {
            throw new RuntimeException("姓名必须为2-4个汉字");
        }

        if (!IdCardValidator.isValid(idCard)) {
            throw new RuntimeException("身份证号码不合法");
        }

        // 获取关联的User对象
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            student = new Student();
            student.setUserId(userId);
            student.setUser(user);  // 设置关联的User对象
        } else {
            // 重新从数据库加载最新数据
            student = studentRepository.findById(student.getUserId()).orElseThrow(
                    () -> new RuntimeException("学生记录不存在")
            );
        }

        student.setRealName(realName);
        student.setGender(gender);
        student.setIdCard(idCard);
        student.setNativePlaceName(nativePlaceName);
        student.setBirthday(birthday);
        student.setGrade(grade);
        student.setNeeds(needs);

        try {
            return studentRepository.save(student);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("数据已被其他操作修改，请刷新页面后重试");
        }
    }

    @Override
    public Student getProfile(Integer userId) {
        return studentRepository.findByUserId(userId);
    }
}