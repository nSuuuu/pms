package com.niit.repository;

import com.niit.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    /**
     * 根据用户ID查询老师信息
     * @param userId 用户ID
     * @return 老师对象
     */
    Teacher findByUserId(Integer userId);
}