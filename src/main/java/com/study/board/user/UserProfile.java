package com.study.board.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_profile") // 테이블 이름 매핑
public class UserProfile {

    @Id
    private String username; // Primary Key로 설정
    private String profileImage;

    // 생성자
    public UserProfile(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }

    public UserProfile() {
    }

    // Getter와 Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

