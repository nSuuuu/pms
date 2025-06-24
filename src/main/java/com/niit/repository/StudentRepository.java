package com.niit.repository;

import com.niit.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    /**
     * 根据用户ID查询学生信息
     * @param userId 用户ID
     * @return 学生对象
     */
    Student findByUserId(Integer userId);
}