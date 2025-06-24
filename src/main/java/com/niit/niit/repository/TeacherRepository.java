package com.niit.niit.repository;

import com.niit.dto.TeacherInfoDTO;
import com.niit.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher findByUserId(Integer userId);

    @Query("SELECT new com.niit.dto.TeacherInfoDTO(t.userId, u.realName, t.avatar, t.experience, t.subjects, t.education, t.style, t.score, t.gradeLevel, t.price, u.province, u.city, u.phone) " +
            "FROM Teacher t " +
            "JOIN User u ON t.userId = u.id " +
            "WHERE u.role = 1 " +
            "AND (:gradeLevel IS NULL OR :gradeLevel = '' OR :gradeLevel = '全部' OR :gradeLevel = 'all' OR :gradeLevel = '全部年级' OR t.gradeLevel = :gradeLevel) " +
            "AND (:subject IS NULL OR t.subjects LIKE %:subject%) " +
            "AND (:province IS NULL OR u.province = :province) " +
            "AND (:city IS NULL OR u.city = :city) " +
            "AND (:minPrice IS NULL OR t.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR t.price <= :maxPrice) " +
            "ORDER BY t.score DESC, t.userId DESC")
    List<TeacherInfoDTO> findTeachersWithFilters(
            @Param("gradeLevel") String gradeLevel,
            @Param("subject") String subject,
            @Param("province") String province,
            @Param("city") String city,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );
}