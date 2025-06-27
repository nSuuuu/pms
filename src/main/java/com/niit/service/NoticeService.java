package com.niit.service;

import com.niit.entity.Notice;
import java.util.List;

public interface NoticeService {
    Notice sendNotice(Integer userId, String title, String content, Notice.NoticeType type);
    Notice sendNoticeWithRef(Integer userId, String title, String content,
                             Notice.NoticeType type, Integer refId, Notice.RefType refType);
    List<Notice> getNoticesByUser(Integer userId);
    List<Notice> getUnreadNoticesByUser(Integer userId);
    int getUnreadCount(Integer userId);
    void markAsRead(Integer noticeId);
    void markAllAsRead(Integer userId);
}