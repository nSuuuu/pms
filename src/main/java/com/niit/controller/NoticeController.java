package com.niit.controller;

import com.niit.entity.Notice;
import com.niit.entity.User;
import com.niit.repository.NoticeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NoticeController {
    @Autowired
    private NoticeRepository noticeRepository;

    @GetMapping("/notices")
    public String noticeList(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        List<Notice> notices = noticeRepository.findByUserIdOrderByCreateTimeDesc(user.getId());
        model.addAttribute("notices", notices);
        return "notices";
    }

    @PostMapping("/notices/read")
    public String markRead(@RequestParam Integer noticeId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Notice notice = noticeRepository.findById(noticeId).orElse(null);
        if (notice != null && notice.getUser().getId().equals(user.getId())) {
            notice.setRead(true);
            noticeRepository.save(notice);
        }
        return "redirect:/notices";
    }
} 