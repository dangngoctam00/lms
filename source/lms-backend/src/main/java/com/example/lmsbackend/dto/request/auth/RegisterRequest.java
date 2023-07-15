package com.example.lmsbackend.dto.request.auth;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;
    private String firstname;
    private String lastname;
    @Length(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    @Length(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String rePassword;
    private String email;
    private String phone;
    private String avatar;
}
