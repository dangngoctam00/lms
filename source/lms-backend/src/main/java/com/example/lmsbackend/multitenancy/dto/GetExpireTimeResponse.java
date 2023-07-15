package com.example.lmsbackend.multitenancy.dto;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetExpireTimeResponse extends BaseResponse {
    private LocalDateTime expireTime;
}
