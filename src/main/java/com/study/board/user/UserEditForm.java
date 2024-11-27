package com.study.board.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserEditForm
{   private String nickname;
    private String intro;
    private String name;
    private String year;
    private String month;
    private String day;
    private String gender;
    public LocalDate getBirthdate() {
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

    }
}
