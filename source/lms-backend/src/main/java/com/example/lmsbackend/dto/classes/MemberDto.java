package com.example.lmsbackend.dto.classes;

import lombok.Data;

@Data
public class MemberDto {
    private String firstName;
    private String lastName;
    private String username;
    private String description;
    private String email;
    private String phone;
    private Long userId;
    private String avatar;
    private String address;
    private String role;

    public MemberDto() {
    }

    public MemberDto(String firstName,
                     String lastName,
                     String username,
                     String email,
                     String phone,
                     Long userId,
                     String avatar,
                     String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.userId = userId;
        this.avatar = avatar;
        this.address = address;
    }
}
