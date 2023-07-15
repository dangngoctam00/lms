package com.example.lmsbackend.multitenancy.dto;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

@Data
public class GetPaymentURLResponse extends BaseResponse {
    private String paymentUrl;
}
