package com.example.lmsbackend.multitenancy.payment_gateway.momo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPaymentMethodRequest {
    private String partnerCode;
    private String partnerName;
    private String storeId;
    private String requestId;
    private Long amount;
    private String orderId;
    private String orderInfo;
    private String orderGroupId;
    private String redirectUrl;
    private String ipnUrl;
    private String requestType;
    private String extraData;
    private boolean autoCapture;
    private String lang;
    private String signature;

//    private String partnerCode;
//    private String orderId;
//    private String orderInfo;
//    private String accessKey;
//    private String amount;
//    private String signature;
//    private String extraData;
//    private String requestId;
//
//    private String notifyUrl;
//    private String returnUrl;
//    private String requestType;
}
