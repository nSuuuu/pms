package com.niit.service.impl;

import com.niit.entity.Notice;
import com.niit.entity.User;
import com.niit.repository.NoticeRepository;
import com.niit.repository.UserRepository;
import com.niit.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Notice sendNotice(Integer userId, String title, String content, Notice.NoticeType type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        Notice notice = new Notice();
        notice.setUser(user);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setType(type);
        notice.setRead(false);
        return noticeRepository.save(notice);
    }

    @Override
    public Notice sendNoticeWithRef(Integer userId, String title, String content,
                                    Notice.NoticeType type, Integer refId, Notice.RefType refType) {
        Notice notice = sendNotice(userId, title, content, type);
        notice.setRefId(refId);
        notice.setRefType(refType);
        return noticeRepository.save(notice);
    }

    @Override
    public List<Notice> getNoticesByUser(Integer userId) {
        return noticeRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    @Override
    public List<Notice> getUnreadNoticesByUser(Integer userId) {
        return noticeRepository.findByUserIdAndReadFalseOrderByCreateTimeDesc(userId);
    }

    @Override
    public int getUnreadCount(Integer userId) {
        return noticeRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Integer noticeId) {
        noticeRepository.findById(noticeId).ifPresent(notice -> {
            notice.setRead(true);
            noticeRepository.save(notice);
        });
    }

    @Override
    @Transactional
    public void markAllAsRead(Integer userId) {
        noticeRepository.markAllAsReadByUserId(userId);
    }
}