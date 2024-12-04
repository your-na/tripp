package com.study.board.contoller;

import com.study.board.entity.Board;
import com.study.board.entity.Question;
import com.study.board.service.BoardService;
import com.study.board.service.QuestionService;
import com.study.board.user.SiteUser;
import com.study.board.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final BoardService boardService;
    private final QuestionService questionService;

    @Autowired
    public AdminController(UserService userService, BoardService boardService, QuestionService questionService) {
        this.userService = userService;
        this.boardService = boardService;
        this.questionService = questionService; // 추가된 부분
    }
    @GetMapping("/posts")
    public String adminDashboard(Model model) {
        List <Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "board_manage";
    }
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<SiteUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user_manage";
    }
    @GetMapping("/support")
    public String adminSupport(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        return "support_manage";
    }

}
