package com.niit.service;

import com.niit.entity.Notice;
import java.util.List;

public interface NoticeService {
    Notice sendNotice(Integer userId, String content);
    List<Notice> getNoticesByUser(Integer userId);
    void markAsRead(Integer noticeId);
} 