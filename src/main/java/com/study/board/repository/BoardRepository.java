package com.study.board.repository;

import com.study.board.entity.Board;
import com.study.board.user.SiteUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer> {
    Page<Board> findAll(Pageable pageable);
    Page<Board> findAll(Specification<Board> spec, Pageable pageable);
    // 댓글 수 기준으로 상위 10개 가져오기
    @Query("SELECT b FROM Board b ORDER BY SIZE(b.commentList) DESC")
    List<Board> findTop10ByCommentCount();

    // 좋아요 수 기준으로 상위 10개 가져오기
    @Query("SELECT b FROM Board b ORDER BY SIZE(b.voter) DESC")
    List<Board> findTop10ByVoterCount();

    // 혼합 점수 기반 정렬
    @Query("""
        SELECT b FROM Board b
        ORDER BY 
            (SIZE(b.commentList) * 0.5 + SIZE(b.voter) * 0.5) DESC
    """)
    List<Board> findTop10ByRankingScore();
    List<Board> findByAuthor(SiteUser author);
}
