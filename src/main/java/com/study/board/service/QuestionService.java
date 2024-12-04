package com.study.board.service;

import com.study.board.DataNotFoundException;
import com.study.board.entity.Board;
import com.study.board.entity.Question;
import com.study.board.repository.QuestionRepository;
import com.study.board.user.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public void write(Question question) {
        questionRepository.save(question);
    }
    public List<Question> getQuestionsByAuthor(SiteUser author) {
        return questionRepository.findByAuthor(author);
    }
    public Question getQuestionById(int id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("board not found");
        }

    }
    // 특정 게시글 불러오기
    public Question question(int id) {
        return questionRepository.findById(id).orElse(null);
    }
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}
