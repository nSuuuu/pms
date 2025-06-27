package com.niit.controller;

import com.niit.entity.Notice;
import com.niit.entity.User;
import com.niit.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notices")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public String noticeList(
            @RequestParam(required = false) Integer userId,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("user");
        Integer targetUserId = (userId != null) ? userId : (user != null ? user.getId() : null);

        if (targetUserId == null) return "redirect:/login";

        List<Notice> notices = noticeService.getNoticesByUser(targetUserId);
        int unreadCount = noticeService.getUnreadCount(targetUserId);

        model.addAttribute("notices", notices);
        model.addAttribute("unreadCount", unreadCount);
        return "notices";
    }

    @PostMapping("/read")
    public String markRead(@RequestParam Integer noticeId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        noticeService.markAsRead(noticeId);
        return "redirect:/notices";
    }

    @PostMapping("/read-all")
    public String markAllRead(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        noticeService.markAllAsRead(user.getId());
        return "redirect:/notices";
    }
}