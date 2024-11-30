package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.entity.BoardImage;
import com.study.board.repository.BoardImageRepository;
import com.study.board.user.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.study.board.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.study.board.DataNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardImageRepository boardImageRepository;

    // 글 작성 처리
    public void write(Board board) {
        boardRepository.save(board);
    }

    public Page<Board> getList(int page, String kw) {
        Pageable pageable = PageRequest.of(page, 10);
        Specification<Board> spec = search(kw);
        return this.boardRepository.findAll(spec,pageable);
    }
    // 특정 게시글 불러오기
    public Board boardView(int id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 게시글 삭제하기
    public void boardDelete(int id) {
        boardRepository.deleteById(id);
    }

    public Board getBoard(Integer id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }

    public void vote(Board board, SiteUser siteUser) {
        board.getVoter().add(siteUser);
        this.boardRepository.save(board);
    }

    private Specification<Board> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Board> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Board, SiteUser> u1 = q.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%")    // 작성자
                );
            }
        };
    }

    public BoardService(BoardImageRepository boardImageRepository) {
        this.boardImageRepository = boardImageRepository;
    }

    @Value("${file.boardImagePath}")
    private String uploadFolder;

    public void saveBoardImages(Board board, List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String imageFileName = uuid + "_" + file.getOriginalFilename();
                File destinationFile = new File(uploadFolder + imageFileName);

                // 파일을 지정된 위치에 저장
                file.transferTo(destinationFile);

                // BoardImage 엔티티 생성 후 DB에 저장
                BoardImage image = new BoardImage();
                image.setUrl("/boardImageUpload/" + imageFileName); // 이미지 URL
                image.setBoard(board); // 게시글과 연결
                boardImageRepository.save(image);
                System.out.println("보드서비스 이미지저장");
            }
        }
    }
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
    public List<Board> getBoardsByUser(SiteUser user) {
        return boardRepository.findByAuthor(user);
    }
    public List<Board> getBoardsLikedByUser(SiteUser user) {
        return boardRepository.findByVoter(user);
    }
    // 댓글 수 기준 랭킹
    public List<Board> getTopRankedByComments() {
        return boardRepository.findTop10ByCommentCount();
    }

    // 좋아요 수 기준 랭킹
    public List<Board> getTopRankedByLikes() {
        return boardRepository.findTop10ByVoterCount();
    }

    // 혼합 점수 기준 랭킹
    public List<Board> getTopRankedByScore() {
        return boardRepository.findTop10ByRankingScore();
    }
}
