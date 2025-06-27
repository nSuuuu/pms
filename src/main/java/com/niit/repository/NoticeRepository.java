package com.niit.repository;

import com.niit.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer>, NoticeRepositoryCustom {
    List<Notice> findByUserIdOrderByCreateTimeDesc(Integer userId);
    List<Notice> findByUserIdAndReadFalseOrderByCreateTimeDesc(Integer userId);
    int countByUserIdAndReadFalse(Integer userId);
    List<Notice> findByUserIdAndTypeOrderByCreateTimeDesc(Integer userId, Notice.NoticeType type);
}