package com.study.board.user;

import com.study.board.entity.BoardImage;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "site_user")
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String nickname;

    private String phone;

    private String email;

    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    private String intro;

    private String gender;

    @Column(nullable = false)
    private String address;

    @OneToOne(mappedBy = "siteUser", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private UserImage userImage;

    public SiteUser() {
    }

    public static SiteUser from(UserCreateForm userCreateForm, PasswordEncoder passwordEncoder) {
        LocalDate birthdate = userCreateForm.getBirthdateAsLocalDate();
        if (birthdate == null) {
            throw new IllegalArgumentException("Invalid birthdate");
        }

        return SiteUser.builder()
                .username(userCreateForm.getUsername())
                .email(userCreateForm.getEmail())
                .password(passwordEncoder.encode(userCreateForm.getPassword1()))
                .nickname(userCreateForm.getNickname())
                .phone(userCreateForm.getPhone())
                .name(userCreateForm.getName())
                .birthdate(birthdate)
                .address(userCreateForm.getAddress())
                .build();
    }
}
