package com.example.lmsbackend.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LimitPermission {
    private LocalDateTime limitStart;
    private LocalDateTime limitEnd;
    private LocalDateTime unlimitStart;
    private LocalDateTime unlimitEnd;
}
