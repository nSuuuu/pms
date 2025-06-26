package com.niit.service;

import com.niit.entity.Student;
import com.niit.entity.Teacher;
import com.niit.entity.User;
import com.niit.entity.User.Gender;
import com.niit.utils.BusinessException;
import com.niit.repository.StudentRepository;
import com.niit.repository.TeacherRepository;
import com.niit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public User login(String phone, String password) throws BusinessException {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            throw new BusinessException("手机号未注册");
        }
        // 直接比较明文密码
        if (!password.equals(user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        return user;
    }

    @Transactional
    public User register(User user, Integer roleType, String extraInfo) throws BusinessException {
        // 验证手机号是否已存在
        if (userRepository.countByPhone(user.getPhone()) > 0) {
            throw new BusinessException("手机号已被注册");
        }

        // 验证用户名是否已存在
        if (userRepository.countByUsername(user.getUsername()) > 0) {
            throw new BusinessException("用户名已被使用");
        }

        // 设置默认值
        user.setGender(Gender.男); // 默认性别
        user.setProvince("未知"); // 默认省份
        user.setCity("未知");     // 默认城市
        user.setIdCard(null);      // 默认空身份证
        user.setRealName(null);    // 真实姓名默认为空，后续完善
        user.setRole(roleType);  // 设置角色

        // 存储明文密码
        user.setPassword(user.getPassword());

        // 插入用户
        user = userRepository.save(user);

        // 根据角色插入额外信息
        if (roleType == 1) { // 老师
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setScore(5.0f); // 默认评分5分
            teacherRepository.save(teacher);
        } else if (roleType == 2) { // 学生
            Student student = new Student();
            student.setUser(user);
            student.setGrade(extraInfo); // 使用extraInfo作为年级
            studentRepository.save(student);
        }

        return user;
    }

    @Transactional
    public void updateStudentProfile(Integer userId, String realName, String gender, String idCard, String province, String city, String grade, String needs) throws BusinessException {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setRealName(realName);
        user.setGender(User.Gender.valueOf(gender));
        user.setIdCard(idCard);
        user.setProvince(province);
        user.setCity(city);
        userRepository.save(user);
        Student student = studentRepository.findByUserId(userId);
        if (student == null) {
            student = new Student();
            student.setUserId(userId);
        }
        student.setGrade(grade);
        student.setNeeds(needs);
        studentRepository.save(student);
    }

    @Transactional
    public void updateTeacherProfile(Integer userId, String realName, String gender, String idCard, String province, String city, String avatar, String subjects, String education, String style) throws BusinessException {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setRealName(realName);
        user.setGender(User.Gender.valueOf(gender));
        user.setIdCard(idCard);
        user.setProvince(province);
        user.setCity(city);
        userRepository.save(user);
        Teacher teacher = teacherRepository.findByUserId(userId);
        if (teacher == null) {
            teacher = new Teacher();
            teacher.setUserId(userId);
        }
        teacher.setAvatar(avatar);
        teacher.setSubjects(subjects);
        teacher.setEducation(education);
        teacher.setStyle(style);
        teacherRepository.save(teacher);
    }

    public boolean isIdCardExists(String idCard, Integer id) {
    return userRepository.existsByIdCardAndIdNot(idCard, id);}

    public User getUserById(Integer id) {
    return userRepository.findById(id).orElse(null);}
}