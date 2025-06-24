package com.niit.mapper;

import com.niit.entity.Teacher;
import com.niit.dto.TeacherInfoDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("""
        SELECT 
            t.user_id,
            u.real_name,
            t.avatar,
            t.experience,
            t.subjects,
            t.education,
            t.style,
            t.score,
            t.grade_level,
            t.price,
            u.province,
            u.city,
            u.phone
        FROM teachers t
        JOIN users u ON t.user_id = u.id
        WHERE u.role = 1
        AND (#{gradeLevel} IS NULL OR #{gradeLevel} = '' OR #{gradeLevel} = '全部' OR #{gradeLevel} = 'all' OR #{gradeLevel} = '全部年级' OR t.grade_level = #{gradeLevel})
        AND (#{subject} IS NULL OR t.subjects LIKE CONCAT('%', #{subject}, '%'))
        AND (#{province} IS NULL OR u.province = #{province})
        AND (#{city} IS NULL OR u.city = #{city})
        AND (#{minPrice} IS NULL OR t.price >= #{minPrice})
        AND (#{maxPrice} IS NULL OR t.price <= #{maxPrice})
        ORDER BY t.score DESC, t.user_id DESC
    """)
    List<TeacherInfoDTO> findTeachersWithFilters(
        @Param("gradeLevel") String gradeLevel,
        @Param("subject") String subject,
        @Param("province") String province,
        @Param("city") String city,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice
    );
}