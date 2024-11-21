package com.study.board.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter
@Setter
public class UserCreateForm {

    @Size(min = 3, max = 25, message = "아이디는 3자 이상 25자 이하로 입력해주세요.")
    @NotEmpty(message = "아이디는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    private String nickname;

    @NotEmpty(message = "전화번호는 필수항목입니다.")
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식은 010-1234-5678이어야 합니다.")
    private String phone;

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String name;

    // 생년월일을 String으로 받아올 때, 예를 들어 'yyyy-MM-dd' 형태로 받음
    @NotEmpty(message = "생년월일은 필수항목입니다.")
    private String birthdate;

    @NotEmpty(message = "거주지는 필수항목입니다.")
    private String address;

    // 생년월일을 LocalDate로 반환하는 메서드 추가
    public LocalDate getBirthdateAsLocalDate() {
        if (birthdate != null && !birthdate.isEmpty()) {
            try {
                return LocalDate.parse(birthdate);  // 'yyyy-MM-dd' 형식으로 변환 시도
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("생년월일을 올바른 형식으로 입력해주세요.");
            }
        }
        return null;  // 생년월일이 비어있으면 null 반환
    }
}
