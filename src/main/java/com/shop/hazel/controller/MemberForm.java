package com.shop.hazel.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberForm {

    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;

    @NotEmpty(message = "비밀번호 확인을 입력하세요")
    private String confirmPassword;

    private String city;
    private String street;
    private String zipcode;
}
