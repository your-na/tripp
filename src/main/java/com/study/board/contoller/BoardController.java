package com.study.board.contoller;
import com.study.board.entity.Board;
import com.study.board.entity.BoardImage;
import com.study.board.repository.BoardImageRepository;
import com.study.board.repository.BoardRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardImageRepository boardImageRepository;

    @GetMapping("/main")
    public String mainPage(Model model) {
        // 1. 전체 scoreRanking 리스트 가져오기
        List<Board> scoreRanking = boardService.getTopRankedByScore();
        // 2. 최대 4개까지만 가져오기
        List<Board> limitedScoreRanking = scoreRanking.stream()
                .limit(4)  // 최대 4개 항목으로 제한
                .collect(Collectors.toList());
        // 3. 모델에 제한된 리스트 추가
        model.addAttribute("scoreRanking", limitedScoreRanking);
        return "/main";
    }

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }
    //게시글 리스트
    @GetMapping("/board/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,@RequestParam(value="kw", defaultValue="") String kw) {
        Page<Board> paging = this.boardService.getList(page,kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "boardList";
    }
    @GetMapping("/board/search")
    public String search(@RequestParam(value = "kw", defaultValue = "") String kw,
                         @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        // 검색어와 페이지 정보를 이용해 게시글 리스트 가져오기
        Page<Board> paging = boardService.getList(page, kw);

        // 모델에 검색어와 페이지 정보를 추가
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        // 검색 결과를 출력할 페이지를 반환
        return "search"; // search.html 파일을 렌더링
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
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
    public String boardWritePro(Board board, Model model, Principal principal ,@RequestParam(value = "files", required = false) List<MultipartFile> files) {
        // 로그인한 사용자의 SiteUser 객체를 가져옴
        SiteUser siteUser = this.userService.getUser(principal.getName());

        board.setAuthor(siteUser);
        // 게시글을 저장
        try {
            // 게시글 저장
            boardService.write(board);
            boardService.saveBoardImages(board, files);

            System.out.println("보드컨트롤러 이미지저장");
        } catch (Exception e) {
            model.addAttribute("error", "파일 업로드 중 문제가 발생했습니다.");
            System.out.println("보드서비스 이미지저장중 오류"+e.getMessage());
            return "boardwrite"; // 에러 발생 시 글 작성 화면으로 돌아가기
        }

        // 작성 완료 메시지와 리다이렉트 URL 설정
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/view?id=" + board.getId());

        return "message"; // "message.html" 템플릿 파일을 반환
    }

    // 게시글 세부내용
    @GetMapping("/board/view")
    public String boardView(Model model, @RequestParam("id") int id, Principal principal) {
        Board board = boardService.boardView(id);
        model.addAttribute("board", board);

        // 로그인된 사용자가 있을 경우 투표 여부 확인
        if (principal != null) {
            SiteUser siteUser = userService.getUser(principal.getName());
            boolean hasVoted = boardService.hasVoted(board, siteUser);
            model.addAttribute("hasVoted", hasVoted);
        }

        List<BoardImage> images = boardImageRepository.findByBoardId(id);
        model.addAttribute("images", images);
        return "boardview";
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
    public String boardVote(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.boardService.vote(board, siteUser);
        return String.format("redirect:/board/view?id=%d", id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/save/{id}")
    public String boardSave(Principal principal, @PathVariable("id") Integer id) {
        Board board = this.boardService.getBoard(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.boardService.save(board, siteUser);
        return String.format("redirect:/board/view?id=%d", id);
    }
    // 댓글 수 랭킹 페이지
    @GetMapping("/ranking/comments")
    public String getCommentRanking(Model model) {

        model.addAttribute("commentRanking", boardService.getTopRankedByComments());
        return "ranking_comments"; // ranking_comments.html로 이동
    }

    // 좋아요 수 랭킹 페이지
    @GetMapping("/ranking/likes")
    public String getLikeRanking(Model model) {
        model.addAttribute("likeRanking", boardService.getTopRankedByLikes());
        return "ranking_likes"; // ranking_likes.html로 이동
    }

    // 혼합 점수 랭킹 페이지
    @GetMapping("/board/hot")
    public String getScoreRanking(Model model) {
        model.addAttribute("scoreRanking", boardService.getTopRankedByScore());
        return "board_hot"; }
}
