package com.study.board.contoller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import com.study.board.user.SiteUser;
import com.study.board.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final BoardService boardService;

    @Autowired
    public AdminController(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
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
}
