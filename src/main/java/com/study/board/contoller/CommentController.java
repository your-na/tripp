package com.study.board.contoller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import com.study.board.service.CommentService;
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
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/create/{id}")
    public String createComment(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content, Principal principal) {
        Board board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.commentService.create(board, content,siteUser);
        return String.format("redirect:/board/view?id=%s", id);
    }
}
