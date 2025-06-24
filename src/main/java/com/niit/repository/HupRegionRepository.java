package com.niit.repository;

import com.niit.entity.HupRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HupRegionRepository extends JpaRepository<HupRegion, Integer> {
    @Query("SELECT r FROM HupRegion r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.pinyin) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "r.code LIKE CONCAT('%', :keyword, '%') OR " +
            "r.zip_code LIKE CONCAT('%', :keyword, '%') OR " +
            "LOWER(r.merger_name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<HupRegion> findByNameContainingOrPinyinContainingOrCodeContaining(String keyword);
}