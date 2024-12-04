package com.study.board.service;

import com.study.board.entity.Answer;
import com.study.board.entity.Question;
import com.study.board.repository.AnswerRepository;
import com.study.board.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    public void create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);

    }
}
