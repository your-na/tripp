package com.study.board.contoller;

import com.study.board.entity.Question;
import com.study.board.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class QuestionController {
    private final QuestionService questionService;
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService; // 추가된 부분
    }
    // 게시글 세부내용
    @GetMapping("/question/view")
    public String questionView(Model model, @RequestParam("id") int id) {
        Question question = questionService.question(id);
        model.addAttribute("question", question);
        return "question_view"; // 뷰를 담당하는 템플릿 파일 이름 리턴에 써주면 된다..
    }
}
