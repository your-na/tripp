package com.study.board.contoller;

import com.study.board.entity.Notification;
import com.study.board.service.NotificationService;
import com.study.board.user.SiteUser;
import com.study.board.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notifications")
    public String notifications(Model model, Principal principal) {
        // 현재 로그인된 사용자 가져오기
        SiteUser siteUser = userService.getUser(principal.getName());
        // 사용자와 관련된 알림 목록 가져오기
        List<Notification> notifications = notificationService.getNotificationsByUser(siteUser);

        // 알림을 모델에 추가
        model.addAttribute("notifications", notifications);

        return "notifications"; // 알림 페이지로 이동
    }
}
