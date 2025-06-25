package com.niit.repository;

import com.niit.entity.ChatMessage;
import com.niit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByFromUserIdAndToUserIdOrderByCreateTimeAsc(Integer fromUserId, Integer toUserId);
    List<ChatMessage> findByToUserIdAndReadFalse(Integer toUserId);

    List<ChatMessage> findByFromUser(User user);
    List<ChatMessage> findByToUser(User user);
} 