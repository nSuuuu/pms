package com.niit.service.impl;

import com.niit.entity.Notice;
import com.niit.entity.User;
import com.niit.repository.NoticeRepository;
import com.niit.repository.UserRepository;
import com.niit.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Notice sendNotice(Integer userId, String content) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        Notice notice = new Notice();
        notice.setUser(user);
        notice.setContent(content);
        notice.setRead(false);
        return noticeRepository.save(notice);
    }

    @Override
    public List<Notice> getNoticesByUser(Integer userId) {
        return noticeRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    @Override
    public void markAsRead(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);
        if (notice != null) {
            notice.setRead(true);
            noticeRepository.save(notice);
        }
    }
} 