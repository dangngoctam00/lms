package com.example.lmsbackend.multitenancy.payment_gateway.momo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetPaymentMethodResponse implements Serializable {
    private String partnerCode;
    private String requestId;
    private String orderId;
    private Long amount;
    private Long responseTime;
    private String message;
    private int resultCode;
    private String payUrl;
    private String deeplink;
    private String qrCodeUrl;
    private String deeplinkMiniApp;
}
