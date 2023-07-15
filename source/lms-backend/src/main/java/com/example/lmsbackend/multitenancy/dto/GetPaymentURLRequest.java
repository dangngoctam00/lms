package com.example.lmsbackend.multitenancy.dto;

import com.example.lmsbackend.multitenancy.enums.PaymentGateWay;
import lombok.Data;

@Data
public class GetPaymentURLRequest {
    private PaymentGateWay gateway;
    private int packageId;
}
