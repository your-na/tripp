package com.study.board.contoller;
import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import com.study.board.user.SiteUser;
import com.study.board.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.security.Principal;
@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    //게시글 리스트
    @GetMapping("/board/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,@RequestParam(value="kw", defaultValue="") String kw) {
        Page<Board> paging = this.boardService.getList(page,kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "boardList";
    }

    //게시글작성
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/write")
    public String boardWriteForm(){
        return "boardwrite";
    }

    //게시글 작성완료
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, Principal principal) {
        // 로그인한 사용자의 SiteUser 객체를 가져옴
        SiteUser siteUser = this.userService.getUser(principal.getName());

        board.setAuthor(siteUser);
        // 게시글을 저장
        boardService.write(board);

        // 작성 완료 메시지와 리다이렉트 URL 설정
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/view?id=" + board.getId());

        return "message"; // "message.html" 템플릿 파일을 반환
    }

    // 게시글 세부내용
    @GetMapping("/board/view")
    public String boardView(Model model, @RequestParam("id") int id) {
        model.addAttribute("board", boardService.boardView(id));
        // localhost/board/view?id=1
        return "boardView"; // 뷰를 담당하는 템플릿 파일 이름 리턴에 써주면 된다..
    }
    //게시글 삭제
    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam("id") int id){
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }
    //게시글 수정
    @GetMapping("board/modify/{id}")
    public String boardModify(@PathVariable("id") int id, Model model) {

        model.addAttribute("board",boardService.boardView(id));

        return "boardmodify";
    }
    //게시글 업데이트
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") int id, Board board,Principal principal){
        Board boardTemp =boardService.boardView(id);// 기존 내용 가져오기
        if (!boardTemp.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        boardTemp.setTitle(board.getTitle()); //덮어 싀우기
        boardTemp.setContent(board.getContent()); //덮어 싀우기

        boardService.write(boardTemp);
        return "redirect:/board/view?id=" + id;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Board question = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.boardService.vote(question, siteUser);
        return String.format("redirect:/board/view?id=%d", id);
    }

}
