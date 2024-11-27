package com.study.board.user;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditForm {
    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;


    @NotEmpty(message = "자기소개는 필수항목입니다.")
    private String intro;

    @NotEmpty(message = "자기소개는 필수항목입니다.")
    private String name;

    @NotEmpty(message = "이미지 경로는 필수항목입니다.") // 기본적으로 이미지 URL을 요구하는 경우
    private String profileImage;

    @NotEmpty(message = "성별은 필수항목입니다.")
    private String gender;

    public UserEditForm() {
    }
}
