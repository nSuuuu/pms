package com.niit.controller;

import com.niit.entity.ChatMessage;
import com.niit.entity.User;
import com.niit.repository.ChatMessageRepository;
import com.niit.repository.UserRepository;
import com.niit.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Controller
public class ChatController {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    public String chatPage(@RequestParam(value = "toUserId", required = false) Integer toUserId, HttpSession session, Model model) {
        if (toUserId == null) {
            return "redirect:/";
        }
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        User toUser = userRepository.findById(toUserId).orElse(null);
        if (toUser == null) return "redirect:/";
        List<ChatMessage> messages = chatService.getChatHistory(user.getId(), toUserId);
        model.addAttribute("messages", messages);
        model.addAttribute("toUser", toUser);
        List<ChatMessage> unreadList = chatMessageRepository.findByToUserIdAndReadFalse(user.getId());
        int unreadCount = unreadList != null ? unreadList.size() : 0;
        model.addAttribute("unreadCount", unreadCount);
        return "chat";
    }

    @PostMapping("/chat/send")
    public String sendMessage(@RequestParam Integer toUserId, @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        User toUser = userRepository.findById(toUserId).orElse(null);
        if (toUser == null) return "redirect:/";
        chatService.sendMessage(user.getId(), toUserId, content);
        return "redirect:/chat?toUserId=" + toUserId;
    }

    @GetMapping("/chat/list")
    public String chatList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        // 查询所有和当前用户有关的消息
        List<ChatMessage> allMessages = chatMessageRepository.findAll();
        // 用于存储会话用户id和用户对象
        Map<Integer, User> chatUsers = new LinkedHashMap<>();
        for (ChatMessage msg : allMessages) {
            if (msg.getFromUser().getId().equals(user.getId())) {
                // 我发给别人的
                chatUsers.put(msg.getToUser().getId(), msg.getToUser());
            } else if (msg.getToUser().getId().equals(user.getId())) {
                // 别人发给我的
                chatUsers.put(msg.getFromUser().getId(), msg.getFromUser());
            }
        }
        model.addAttribute("chatUsers", chatUsers.values());
        return "chat_list";
    }
} 