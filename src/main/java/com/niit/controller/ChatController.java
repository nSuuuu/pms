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

import java.util.*;

@Controller
public class ChatController {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatService chatService;

    @GetMapping("/chat")
    public String chatPage(@RequestParam(value = "toUserId", required = false) Integer toUserId,
                           HttpSession session, Model model) {
        if (toUserId == null) {
            model.addAttribute("error", "未指定目标用户");
            return "error";
        }

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Optional<User> toUserOpt = userRepository.findById(toUserId);
        if (toUserOpt.isEmpty()) {
            model.addAttribute("error", "目标用户不存在");
            return "error";
        }

        User toUser = toUserOpt.get();
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

        List<ChatMessage> sentMessages = chatMessageRepository.findByFromUser(user);
        List<ChatMessage> receivedMessages = chatMessageRepository.findByToUser(user);
        List<ChatMessage> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);

        Set<Integer> userIds = new HashSet<>();
        Map<Integer, User> chatUsers = new LinkedHashMap<>();

        for (ChatMessage msg : allMessages) {
            Integer otherId;
            if (msg.getFromUser().getId().equals(user.getId())) {
                otherId = msg.getToUser().getId();
            } else if (msg.getToUser().getId().equals(user.getId())) {
                otherId = msg.getFromUser().getId();
            } else {
                continue;
            }

            if (!userIds.contains(otherId)) {
                userRepository.findById(otherId).ifPresent(u -> {
                    chatUsers.put(u.getId(), u);
                    userIds.add(u.getId());
                });
            }
        }

        model.addAttribute("chatUsers", chatUsers.values());
        return "chat_list";
    }
} 