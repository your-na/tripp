package com.study.board.service;

import com.study.board.entity.Notification;
import com.study.board.repository.NotificationRepository;
import com.study.board.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    public void sendNotification(SiteUser user, String title, String message,String link) {
        Notification notification = Notification.create(user, title, message,link);
        notificationRepository.save(notification);
    }
    public List<Notification> getNotificationsByUser(SiteUser user) {
        // NotificationRepository를 사용하여 사용자와 연결된 알림 조회
        return notificationRepository.findByUser(user);
    }
}
