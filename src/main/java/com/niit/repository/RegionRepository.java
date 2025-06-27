package com.niit.repository;

import com.niit.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

    @Query("SELECT r FROM Region r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(r.pinyin) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(r.code) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(r.first) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Region> findByNameOrPinyinOrCodeContaining(String q);
}