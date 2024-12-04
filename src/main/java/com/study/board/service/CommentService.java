package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.repository.CommentRepository;
import com.study.board.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    public void create(Board board, String content, SiteUser author) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setBoard(board);
        comment.setAuthor(author);
        this.commentRepository.save(comment);

        SiteUser boardAuthor = board.getAuthor(); // 게시글 작성자 가져오기
        if (!boardAuthor.equals(author)) { // 본인에게는 알림을 보내지 않음
            String title = "새로운 댓글이 달렸습니다!";
            String message = author.getNickname() + "님이 '" + board.getTitle() + "'에 댓글을 남겼습니다.";
            String link = "/board/view?id=" + board.getId(); // 로컬 환경을 기준으로 생성
            notificationService.sendNotification(boardAuthor, title, message,link);
        }

    }
}
