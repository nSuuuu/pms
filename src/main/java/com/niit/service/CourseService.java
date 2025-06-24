package com.niit.service;

import com.niit.entity.Course;
import com.niit.entity.User;
import com.niit.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    /**
     * 根据老师用户对象获取课表
     * @param teacher 老师用户对象
     * @return 课程列表
     */
    public List<Course> getCoursesByTeacher(User teacher) {
        return courseRepository.findByTeacher(teacher);
    }

    /**
     * 根据学生用户对象获取课表
     * @param student 学生用户对象
     * @return 课程列表
     */
    public List<Course> getCoursesByStudent(User student) {
        return courseRepository.findByStudent(student);
    }
}