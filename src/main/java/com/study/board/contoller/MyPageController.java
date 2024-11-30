package com.study.board.contoller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import com.study.board.user.SiteUser;
import com.study.board.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/mypage")
@Controller
public class MyPageController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    @GetMapping("/myboard")
    public String myboard(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserByUsername(username);
        List<Board> userBoards = boardService.getBoardsByUser(user);
        if (userBoards == null) {
            userBoards = new ArrayList<>();
        }
        int postCount = userBoards.size();
        model.addAttribute("boards", userBoards);
        model.addAttribute("postCount", postCount);
        return "myboard";
    }

    @GetMapping("/myheart")
    public String myhehart(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserByUsername(username);
        List<Board> likedBoards =boardService.getBoardsLikedByUser(user);
        if (likedBoards == null) {
            likedBoards = new ArrayList<>();
        }
        int heartCount = likedBoards.size();
        model.addAttribute("likedBoards", likedBoards);
        model.addAttribute("heartCount", heartCount);
        return "myheart";
    }
}
