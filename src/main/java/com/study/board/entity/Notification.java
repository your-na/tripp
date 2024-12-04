package com.study.board.entity;
import com.study.board.user.SiteUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private SiteUser user;

    private String title;

    private String message;

    private String link;
    private LocalDateTime createDate;

    public static Notification create(SiteUser user, String title, String message,String link) {
        return Notification.builder()
                .user(user)
                .title(title)
                .link(link)
                .message(message)
                .createDate(LocalDateTime.now())
                .build();
    }
}