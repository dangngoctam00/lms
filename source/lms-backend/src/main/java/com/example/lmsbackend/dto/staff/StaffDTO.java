package com.example.lmsbackend.dto.staff;

import lombok.Data;

@Data
public class StaffDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatar;
    private String description;
}
