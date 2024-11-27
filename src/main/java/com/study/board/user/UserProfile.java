package com.study.board.user;

import com.study.board.entity.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile") // 테이블 이름 매핑
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "site_user_id")
    private SiteUser siteUser;

}

