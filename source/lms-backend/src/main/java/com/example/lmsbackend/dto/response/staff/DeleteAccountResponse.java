package com.example.lmsbackend.dto.response.staff;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

@Data
public class DeleteAccountResponse extends BaseResponse {
    private Long staffId;
}
