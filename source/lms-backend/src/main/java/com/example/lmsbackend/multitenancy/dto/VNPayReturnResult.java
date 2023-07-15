package com.example.lmsbackend.multitenancy.dto;

import com.example.lmsbackend.multitenancy.enums.VNPayPaymentResultEnum;
import lombok.Data;

@Data
public class VNPayReturnResult {
    private String RspCode;
    private String Message;
    public VNPayReturnResult(VNPayPaymentResultEnum vnPayPaymentResultEnum){
        this.RspCode = vnPayPaymentResultEnum.getRspCode();
        this.Message = vnPayPaymentResultEnum.getMessage();
    }



}
