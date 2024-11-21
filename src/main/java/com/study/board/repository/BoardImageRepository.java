package com.study.board.repository;

import com.study.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findByBoardId(int boardId); // 게시글 ID로 관련된 이미지들을 찾는 메서드
}