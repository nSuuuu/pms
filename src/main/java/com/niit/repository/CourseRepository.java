package com.niit.repository;

import com.niit.entity.Course;
import com.niit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByStudentId(Integer studentId);
    /**
     * 根据老师 ID 查询课表
     * @param teacher 老师用户对象
     * @return 课程列表
     */
    List<Course> findByTeacher(User teacher);

    /**
     * 根据学生 ID 查询课表
     * @param student 学生用户对象
     * @return 课程列表
     */
    List<Course> findByStudent(User student);

    List<Course> findByTeacherId(Integer id);
}