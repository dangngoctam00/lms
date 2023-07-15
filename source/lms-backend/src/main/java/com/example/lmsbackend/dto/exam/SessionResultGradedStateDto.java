package com.example.lmsbackend.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionResultGradedStateDto {
    private UUID sessionId;
    private Boolean isFullyGraded;
}
