package com.niit.mapper;

import com.niit.entity.Student;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StudentMapper {
    /**
     * 插入学生信息
     * @param student 学生对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO students(user_id, grade, needs) VALUES(#{userId}, #{grade}, #{needs})")
    int insert(Student student);

    /**
     * 根据用户ID查询学生信息
     * @param userId 用户ID
     * @return 学生对象
     */
    @Select("SELECT * FROM students WHERE user_id = #{userId}")
    Student findByUserId(@Param("userId") Integer userId);
}