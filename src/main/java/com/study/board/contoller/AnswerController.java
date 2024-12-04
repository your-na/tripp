package com.study.board.contoller;

import com.study.board.entity.Board;
import com.study.board.entity.Question;
import com.study.board.service.AnswerService;
import com.study.board.service.QuestionService;
import com.study.board.user.SiteUser;
import com.study.board.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final AnswerService answerService;
    private final UserService userService;
    private final QuestionService questionService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content, Principal principal) {
        Question question = this.questionService.getQuestionById(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.create(question, content,siteUser);
        return String.format("redirect:/question/view?id=%s", id);
    }
}
