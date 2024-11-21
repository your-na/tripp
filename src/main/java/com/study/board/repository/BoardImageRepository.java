package com.study.board.repository;

import com.study.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    // 추가적인 쿼리 메서드를 정의할 수 있습니다.
}