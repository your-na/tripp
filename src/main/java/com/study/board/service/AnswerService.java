package com.study.board.service;

import com.study.board.entity.Answer;
import com.study.board.entity.Question;
import com.study.board.repository.AnswerRepository;
import com.study.board.repository.NotificationRepository;
import com.study.board.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final NotificationService notificationService;
    public void create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);

        SiteUser questionAuthor = question.getAuthor(); // 게시글 작성자 가져오기
            String title = "문의 답변이 달렸습니다!";
            String message = author.getNickname() + "님이 '" + question.getTitle() + "'에 댓글을 남겼습니다.";
            String link = "mypage/asklist"; // 로컬 환경을 기준으로 생성
            notificationService.sendNotification(questionAuthor, title, message,link);

    }
}
