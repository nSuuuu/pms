package com.niit.controller;

import com.niit.entity.ChatMessage;
import com.niit.entity.User;
import com.niit.repository.ChatMessageRepository;
import com.niit.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/chat")
    public String chatPage(@RequestParam Integer toUserId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        User toUser = userRepository.findById(toUserId).orElse(null);
        if (toUser == null) return "redirect:/";
        List<ChatMessage> messages = chatMessageRepository.findByFromUserIdAndToUserIdOrderByCreateTimeAsc(user.getId(), toUserId);
        messages.addAll(chatMessageRepository.findByFromUserIdAndToUserIdOrderByCreateTimeAsc(toUserId, user.getId()));
        messages.sort((a, b) -> a.getCreateTime().compareTo(b.getCreateTime()));
        model.addAttribute("messages", messages);
        model.addAttribute("toUser", toUser);
        return "chat";
    }

    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam Integer toUserId, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        User toUser = userRepository.findById(toUserId).orElse(null);
        if (toUser == null) return "redirect:/";
        ChatMessage msg = new ChatMessage();
        msg.setFromUser(user);
        msg.setToUser(toUser);
        msg.setContent(content);
        chatMessageRepository.save(msg);
        return "redirect:/chat?toUserId=" + toUserId;
    }
} 