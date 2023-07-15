package com.example.lmsbackend.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubordinateDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDateTime validFrom;
    private LocalDateTime expiresAt;
}
