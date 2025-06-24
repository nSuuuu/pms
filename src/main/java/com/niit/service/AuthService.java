package com.niit.service;

import com.niit.entity.Student;
import com.niit.entity.Teacher;
import com.niit.entity.User;
import com.niit.entity.User.Gender;
import com.niit.utils.BusinessException;
import com.niit.mapper.StudentMapper;
import com.niit.mapper.TeacherMapper;
import com.niit.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    //     Spring Security 提供的一个接口，用于支持密码的编码与匹配。
//     通过使用 PasswordEncoder，可以安全地对用户的密码进行加密存储，并在用户登录时验证密码。
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User login(String phone, String password) throws BusinessException {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            throw new BusinessException("手机号未注册");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        return user;
    }

    @Transactional
    public User register(User user, Integer roleType, String extraInfo) throws BusinessException {
        // 验证手机号是否已存在
        if (userMapper.checkPhoneExists(user.getPhone()) > 0) {
            throw new BusinessException("手机号已被注册");
        }

        // 验证用户名是否已存在
        if (userMapper.checkUsernameExists(user.getUsername()) > 0) {
            throw new BusinessException("用户名已被使用");
        }

        // 设置默认值
        user.setGender(Gender.男);
        user.setProvince("未知");
        user.setCity("未知");
        user.setIdCard("");
        user.setRealName("");
        user.setRole(roleType);

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 插入用户
        userMapper.insert(user);

        // 根据角色插入额外信息
        if (roleType == 1) { // 老师
            Teacher teacher = new Teacher();
            teacher.setUserId(user.getId());
            teacher.setSchoolLevel(extraInfo); // 设置教学阶段
            teacher.setScore(5.0f);
            teacherMapper.insert(teacher);
        } else if (roleType == 2) { // 学生
            Student student = new Student();
            student.setUserId(user.getId());
            student.setGrade(extraInfo); // 设置年级
            studentMapper.insert(student);
        }

        return user;
    }
}