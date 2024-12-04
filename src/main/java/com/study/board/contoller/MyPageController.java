package com.study.board.contoller;

import com.study.board.entity.Board;
import com.study.board.entity.Notification;
import com.study.board.entity.Question;
import com.study.board.service.BoardService;
import com.study.board.service.NotificationService;
import com.study.board.service.QuestionService;
import com.study.board.user.SiteUser;
import com.study.board.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private QuestionService questionService;


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
    public String myheart(Model model, Principal principal) {
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
    @GetMapping("/mypost")
    public String mypost(Model model, Principal principal) {
        String username = principal.getName();
        SiteUser user = userService.getUserByUsername(username);
        List<Board> postBoards =boardService.getPostByUser(user);
        if (postBoards == null) {
            postBoards = new ArrayList<>();
        }
        int postCount = postBoards.size();
        model.addAttribute("postBoards", postBoards);
        model.addAttribute("postCount", postCount);
        return "mypost";
    }
    @GetMapping("/ask")
    public String ask() {
        return "ask";
    }

    @PostMapping("/ask")
    public String ask(Question question, Model model, Principal principal) {
        // 로그인한 사용자의 SiteUser 객체를 가져옴
        SiteUser siteUser = this.userService.getUser(principal.getName());
        question.setAuthor(siteUser);
        questionService.write(question);
        model.addAttribute("message", "문의 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/main");
        return "message"; // "message.html" 템플릿 파일을 반환
    }
    @GetMapping("/asklist")
    public String asklist(Model model, Principal principal) {
        // 로그인한 사용자의 이름을 기반으로 해당 사용자가 작성한 질문을 가져옴
        SiteUser siteUser = userService.getUser(principal.getName());
        List<Question> questions = questionService.getQuestionsByAuthor(siteUser);

        // 모델에 질문 목록 추가
        model.addAttribute("questions", questions);
        return "asklist"; // "asklist.html" 템플릿 파일을 반환
    }

}
