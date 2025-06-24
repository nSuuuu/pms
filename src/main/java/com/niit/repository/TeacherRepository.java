package com.niit.repository;

import com.niit.dto.TeacherInfoDTO;
import com.niit.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    /**
     * 根据用户ID查询老师信息
     * @param userId 用户ID
     * @return 老师对象
     */
    Teacher findByUserId(Integer userId);

    /**
     * 根据多条件查询教师信息
     * @param gradeLevel 年级（可为空）
     * @param subject    科目（可为空，使用LIKE匹配）
     * @param province   省份（可为空）
     * @param city       城市（可为空）
     * @param minPrice   最低价格（可为空）
     * @param maxPrice   最高价格（可为空）
     * @return 教师信息DTO列表
     */
    @Query("SELECT new com.niit.dto.TeacherInfoDTO(" +
            "t.userId, u.realName, t.avatar, t.experience, " +
            "t.subjects, t.education, t.style, t.score, " +
            "t.gradeLevel, t.price, u.province, u.city, u.phone" +
            ") " +
            "FROM Teacher t " +
            "JOIN User u ON t.userId = u.id " +
            "WHERE u.role = 1 " +
            "AND (:gradeLevel IS NULL OR :gradeLevel = '' OR :gradeLevel = '全部' OR :gradeLevel = 'all' OR :gradeLevel = '全部年级' OR t.gradeLevel = :gradeLevel) " +
            "AND (:subject IS NULL OR t.subjects LIKE :subject) " +
            "AND (:province IS NULL OR u.province = :province) " +
            "AND (:city IS NULL OR u.city = :city) " +
            "AND (:minPrice IS NULL OR t.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR t.price <= :maxPrice) " +
            "ORDER BY t.score DESC, t.userId DESC")
    List<TeacherInfoDTO> findTeachersWithFilters(
            String gradeLevel,
            String subject,
            String province,
            String city,
            Integer minPrice,
            Integer maxPrice
    );
}