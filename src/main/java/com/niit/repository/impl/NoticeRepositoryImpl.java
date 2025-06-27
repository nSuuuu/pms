package com.niit.repository.impl;

import com.niit.repository.NoticeRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void markAllAsReadByUserId(Integer userId) {
        entityManager.createNativeQuery("UPDATE notices SET is_read = true WHERE user_id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
}