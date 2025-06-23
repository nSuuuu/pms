package com.niit.mapper;

import com.niit.entity.Teacher;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TeacherMapper {
    /**
     * 插入老师信息
     * @param teacher 老师对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO teachers(user_id, avatar, experience, subjects, education, style, score) " +
            "VALUES(#{userId}, #{avatar}, #{experience}, #{subjects}, #{education}, #{style}, #{score})")
    int insert(Teacher teacher);

    /**
     * 根据用户ID查询老师信息
     * @param userId 用户ID
     * @return 老师对象
     */
    @Select("SELECT * FROM teachers WHERE user_id = #{userId}")
    Teacher findByUserId(@Param("userId") Integer userId);
}