package com.study.board.repository;
import com.study.board.entity.Notification;
import com.study.board.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(SiteUser siteUser);
}