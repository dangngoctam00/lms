package com.example.lmsbackend.dto.students;

import lombok.Data;

@Data
public class StudentInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatar;
}
