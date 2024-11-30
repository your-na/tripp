package com.study.board.user;

import com.study.board.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SiteUser create(UserCreateForm userCreateForm) {
        if (userRepository.existsByUsername(userCreateForm.getUsername())) {
            throw new DataIntegrityViolationException("이미 사용 중인 사용자명입니다.");
        }
        SiteUser user = SiteUser.from(userCreateForm, passwordEncoder);  // SiteUser 객체 생성
        UserImage defaultUserImage = UserImage.builder()
                .url("/userImageUpload/default.jpg")  // 기본 이미지 URL 설정
                .siteUser(user)  // SiteUser와 연관 설정
                .build();
        // SiteUser 객체 저장
        user = userRepository.save(user);

        // UserImage 객체를 저장하기 위해 UserImage의 siteUser 필드에 저장된 User 객체를 설정
        defaultUserImage.setSiteUser(user);

        // UserImage 객체 저장
        userImageRepository.save(defaultUserImage);

        return user;
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

    @Value("${file.userImagePath}")
    private String uploadFolder;

    public void saveUserImages(SiteUser user, List<MultipartFile> files) throws IOException {
        System.out.println("유저서비스 이미지저장 시작전 서비스 호출");
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String imageFileName = uuid + "_" + file.getOriginalFilename();
                File destinationFile = new File(uploadFolder + imageFileName);
                System.out.println("유저서비스 이미지저장 중입니다");
                // 파일을 지정된 위치에 저장
                file.transferTo(destinationFile);

                // UserImage 엔티티 생성 후 DB에 저장
                UserImage image = new UserImage(user.getId(),"/userImageUpload/" + imageFileName,user);
                userImageRepository.save(image);
                System.out.println("유저서비스 이미지저장");
            }
        }
    }
    public List<SiteUser> getAllUsers() {
        return userRepository.findAll();
    }
}