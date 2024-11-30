package com.study.board.user;

import com.study.board.DataNotFoundException;
import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CommentRepository commentRepository;

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
    @Transactional
    public void deleteUser(Principal principal) throws Exception {
        // 현재 로그인한 사용자의 username 가져오기
        String username = principal.getName();

        // 사용자 존재 여부 확인
        SiteUser siteUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));
        if (siteUser != null) {
            // 사용자가 투표한 게시글에서 해당 사용자를 제거
            List<Board> boardVoter = boardRepository.findByVoter(siteUser);
            if (boardVoter != null){
                for (Board board : boardVoter) {
                    board.getVoter().remove(siteUser);
                    boardRepository.save(board); // 변경된 게시글을 저장
                }
            }
            List<Board> boardAuthor = boardRepository.findByAuthor(siteUser);
            if (boardAuthor != null){
                boardRepository.deleteAll(boardAuthor);
            }
            // 사용자가 작성한 댓글 삭제
            List<Comment> comments = commentRepository.findByAuthor(siteUser);
            if (comments != null) {
                commentRepository.deleteAll(comments);
            }

            // 사용자 삭제
            userRepository.deleteByUsername(username);
        } else {
            throw new Exception("User not found");
        }
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
    public SiteUser getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}