package com.example.lmsbackend.multitenancy.dto;

import lombok.Data;

@Data
public class VNPayPaymentResult {
    private String vnp_TmnCode;
    private int vnp_Amount;
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private long vnp_PayDate;
    private String vnp_OrderInfo;
    private long vnp_TransactionNo;
    private String vnp_ResponseCode;
    private int vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_SecureHashType;
    private String vnp_SecureHash;
}
