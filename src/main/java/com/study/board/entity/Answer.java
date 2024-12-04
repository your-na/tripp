package com.study.board.entity;

import com.study.board.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue
    Integer id;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime createDate;
    @ManyToOne
    private Question question;
    @ManyToOne
    private SiteUser author;
}
