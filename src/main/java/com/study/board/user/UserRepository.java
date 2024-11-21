package com.study.board.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username); // 반환 타입을 Optional로 변경

    boolean existsByUsername(@Size(min = 3, max = 25, message = "아이디는 3자 이상 25자 이하로 입력해주세요.") @NotEmpty(message = "아이디는 필수항목입니다.") String username);
}
