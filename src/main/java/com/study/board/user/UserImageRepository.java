package com.study.board.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    List<UserImage> findBySiteUserId(Long Id); // 유저 ID로 관련된 이미지들을 찾는 메서드
}
