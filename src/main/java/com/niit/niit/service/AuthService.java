package com.niit.niit.service;

import com.niit.entity.Student;
import com.niit.entity.Teacher;
import com.niit.entity.User;
import com.niit.entity.User.Gender;
import com.niit.repository.StudentRepository;
import com.niit.repository.TeacherRepository;
import com.niit.repository.UserRepository;
import com.niit.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User login(String phone, String password) throws BusinessException {
        Optional<User> userOptional = userRepository.findByPhone(phone);
        if (!userOptional.isPresent()) {
            throw new BusinessException("手机号未注册");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        return user;
    }

    @Transactional
    public User register(User user, Integer roleType, String extraInfo) throws BusinessException {
        // 验证手机号是否已存在
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new BusinessException("手机号已被注册");
        }

        // 验证用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("用户名已被使用");
        }

        // 设置默认值（除必须字段外，其他设为 NULL 或默认）
        user.setGender(Gender.男); // 默认性别
        user.setProvince("未知"); // 默认省份
        user.setCity("未知");     // 默认城市
        user.setIdCard(null);    // 修改点：身份证号设为 null 而不是 ""
        user.setRealName(null);  // 真实姓名也设为 null
        user.setRole(roleType);  // 设置角色

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 插入用户
        user = userRepository.save(user);

        // 根据角色插入额外信息
        if (roleType == 1) { // 老师
            Teacher teacher = new Teacher();
            teacher.setUserId(user.getId());
            teacher.setScore(5.0f); // 默认评分5分
            teacher.setPrice(100); // 默认价格100元/小时
            teacher.setGradeLevel(extraInfo); // 使用 extraInfo 作为年级
            teacher.setSubjects("数学"); // 默认科目
            teacher.setEducation("本科"); // 默认学历
            teacher.setStyle("耐心细致，因材施教"); // 默认教学风格
            teacher.setExperience("新老师，正在积累教学经验"); // 默认教学经验
            teacher.setAvatar("https://via.placeholder.com/80x80?text=头像"); // 默认头像
            teacherRepository.save(teacher);
        } else if (roleType == 2) { // 学生
            Student student = new Student();
            student.setUserId(user.getId());
            student.setGrade(extraInfo); // 使用 extraInfo 作为年级
            studentRepository.save(student);
        }

        return user;
    }
}