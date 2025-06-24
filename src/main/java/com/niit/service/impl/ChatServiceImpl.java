package com.niit.service.impl;

import com.niit.entity.ChatMessage;
import com.niit.entity.User;
import com.niit.repository.ChatMessageRepository;
import com.niit.repository.UserRepository;
import com.niit.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ChatMessage sendMessage(Integer fromUserId, Integer toUserId, String content) {
        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new RuntimeException("发送方不存在"));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new RuntimeException("接收方不存在"));
        ChatMessage message = new ChatMessage();
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setContent(content);
        message.setRead(false);
        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getChatHistory(Integer userId1, Integer userId2) {
        List<ChatMessage> list1 = chatMessageRepository.findByFromUserIdAndToUserIdOrderByCreateTimeAsc(userId1, userId2);
        List<ChatMessage> list2 = chatMessageRepository.findByFromUserIdAndToUserIdOrderByCreateTimeAsc(userId2, userId1);
        List<ChatMessage> all = new ArrayList<>();
        all.addAll(list1);
        all.addAll(list2);
        all.sort(Comparator.comparing(ChatMessage::getCreateTime));
        return all;
    }
} 