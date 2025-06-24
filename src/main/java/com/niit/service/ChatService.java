package com.niit.service;

import com.niit.entity.ChatMessage;
import java.util.List;
 
public interface ChatService {
    ChatMessage sendMessage(Integer fromUserId, Integer toUserId, String content);
    List<ChatMessage> getChatHistory(Integer userId1, Integer userId2);
} 