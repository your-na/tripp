package com.study.board.entity;

import com.study.board.user.SiteUser;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 200)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Date date;
    public Question() {
        this.date = new Date(); // 게시글 생성 시 현재 날짜로 설정
    }

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Answer> answerList;
    @ManyToOne
    private SiteUser author;
}
