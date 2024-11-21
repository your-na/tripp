package com.study.board.user;

import com.study.board.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository userProfileRepository; // 추가

    @Transactional
    public SiteUser create(UserCreateForm userCreateForm) {
        if (userRepository.existsByUsername(userCreateForm.getUsername())) {
            throw new DataIntegrityViolationException("이미 사용 중인 사용자명입니다.");
        }

        SiteUser user = SiteUser.from(userCreateForm, passwordEncoder);  // SiteUser 객체 생성
        return userRepository.save(user);
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return user.orElse(null);
    }


    public void updateUser(SiteUser siteUser) {
        userRepository.save(siteUser); // 수정된 사용자 정보를 저장
    }

    public SiteUser validateUser(String username, String password) {
        // 데이터베이스에서 사용자 검색 (null 처리)
        Optional<SiteUser> user = userRepository.findByUsername(username);

        // 사용자와 비밀번호 검증
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.orElse(null); // 비밀번호가 일치하면 사용자 반환
        } else {
            return null; // 유효하지 않은 경우 null 반환
        }
    }



    public SiteUser getCurrentUser(String username) {
        Optional<SiteUser> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new DataNotFoundException("현재 사용자를 찾을 수 없습니다.");
        }
        return user.orElse(null);
    }

    // 프로필 이미지 업데이트
    public void updateProfileImage(String username, byte[] imageBytes) {
        SiteUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 이미지 데이터를 엔티티에 저장
        user.setProfileImage(imageBytes);

        // 저장
        userRepository.save(user);
    }

    // 기존에 저장된 이미지가 있다면 삭제 (필요시)
        // userProfileRepository.deleteByUsername(username); // 이전 이미지를 삭제할 경우 이 부분 활성화
    }