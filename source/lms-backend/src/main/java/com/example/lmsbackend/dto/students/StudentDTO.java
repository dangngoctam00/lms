package com.example.lmsbackend.dto.students;

import lombok.Data;

@Data
public class StudentDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatar;
    private String address;
}
