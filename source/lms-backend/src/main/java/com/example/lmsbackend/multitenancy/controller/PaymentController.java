package com.example.lmsbackend.multitenancy.controller;

import com.example.lmsbackend.multitenancy.dto.GetPaymentURLRequest;
import com.example.lmsbackend.multitenancy.dto.GetPaymentURLResponse;
import com.example.lmsbackend.multitenancy.dto.VNPayPaymentResult;
import com.example.lmsbackend.multitenancy.dto.VNPayReturnResult;
import com.example.lmsbackend.multitenancy.service.PaymentService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(API_PREFIX + "/payment")
    public ResponseEntity<GetPaymentURLResponse> getPaymentUrl(@RequestBody GetPaymentURLRequest request) {
        String paymentUrl = paymentService.payment(request);
        GetPaymentURLResponse response = new GetPaymentURLResponse();
        response.setPaymentUrl(paymentUrl);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(originPatterns = "*")
    @GetMapping(API_PREFIX+"/vnpay/payment-return")
    public String handlePaymentVNPayReturn(VNPayPaymentResult paymentResult) {
        log.info("PaymentResult: {}", paymentResult);
        VNPayReturnResult returnResult = paymentService.handleVNPayPaymentReturn(paymentResult);
        return new Gson().toJson(returnResult);
    }
}
