package com.example.lmsbackend.multitenancy.payment_gateway.momo;

import com.example.lmsbackend.multitenancy.payment_gateway.momo.config.MoMoConfig;
import com.example.lmsbackend.multitenancy.payment_gateway.momo.dto.GetPaymentMethodRequest;
import com.example.lmsbackend.multitenancy.payment_gateway.momo.dto.GetPaymentMethodResponse;
import com.example.lmsbackend.multitenancy.payment_gateway.momo.utils.SignatureUtils;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MoMo {
    public static final String REQUEST_TYPE = "captureWallet";
    public static final String REDIRECT_URL = "https://5991-2402-800-6315-490-bd1a-3a57-a4cb-3342.ap.ngrok.io/register/success";
    public static final String IPN_URL = "https://0bf8-2402-800-6315-490-bd1a-3a57-a4cb-3342.ap.ngrok.io/handlePayment";
    private final MoMoConfig moMoConfig;
    private final RestTemplate restTemplate;
    private final SignatureUtils signatureUtils;

    public GetPaymentMethodResponse getPaymentMethod(Long amount, String tenant) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        HashMap<String, String> extraData = new HashMap<>();
        extraData.put("tenant", tenant);
        String extraDataJson = new Gson().toJson(extraData);
        String encodedExtraData = Base64.getEncoder().encode(extraDataJson.getBytes()).toString();
        String requestId = UUID.randomUUID().toString();
        String dataSignature = ("(accessKey=$accessKey&amount=$amount&extraData=$extraData" +
                "&ipnUrl=$ipnUrl&orderId=$orderId&orderInfo=$orderInfo" +
                "&partnerCode=$partnerCode&redirectUrl=$redirectUrl" +
                "&requestId=$requestId&requestType=$requestType, secretKey )")
                .replaceAll("\\$accessKey", moMoConfig.getAccessKey())
                .replaceAll("\\$amount", amount.toString())
                .replaceAll("\\$extraData", encodedExtraData)
                .replaceAll("\\$ipnUrl", IPN_URL)
                .replaceAll("\\$orderId", requestId)
                .replaceAll("\\$orderInfo", "")
                .replaceAll("\\$partnerCode", moMoConfig.getPartnerCode())
                .replaceAll("\\$redirectUrl", REDIRECT_URL)
                .replaceAll("\\$requestId", requestId)
                .replaceAll("\\$requestType", REQUEST_TYPE)
                .replaceAll("^secretKey$", moMoConfig.getSecretKey());

        GetPaymentMethodRequest request = GetPaymentMethodRequest.builder()
                .partnerCode(moMoConfig.getPartnerCode())
                .requestId(requestId)
                .amount(amount)
                .orderId(requestId)
                .orderInfo("")
                .redirectUrl(REDIRECT_URL)
                .ipnUrl(IPN_URL)
                .requestType(REQUEST_TYPE)
                .extraData(encodedExtraData)
                .lang("vi")
                .signature(signatureUtils.sign(dataSignature, moMoConfig.getSecretKey()))
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GetPaymentMethodRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<GetPaymentMethodResponse> responseEntity = restTemplate.exchange(moMoConfig.getApiEndpoint(), HttpMethod.POST, requestEntity, GetPaymentMethodResponse.class);
        return responseEntity.getBody();
    }
}
