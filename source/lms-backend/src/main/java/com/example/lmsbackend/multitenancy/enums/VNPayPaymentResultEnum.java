package com.example.lmsbackend.multitenancy.enums;

import lombok.Getter;

@Getter
public enum VNPayPaymentResultEnum {
    SUCCESS("00","Confirm Success"),
    ORDER_ALREADY_CONFIRM("02","Order already confirmed"),
    INVALID_AMOUNT("04","Invalid Amount"),
    ORDER_NOT_FOUND("01", "Order not Found"),
    INVALID_CHECKSUM("97", "Invalid Checksum"),
    UNKNOWN_ERROR("99","Unknow error");

    private String rspCode;
    private String message;

    VNPayPaymentResultEnum(String rspCode, String message){
        this.rspCode = rspCode;
        this.message = message;
    }
}
