package com.example.lmsbackend.multitenancy.service;

import com.example.lmsbackend.constant.AppConstant;
import com.example.lmsbackend.multitenancy.domain.PackageEntity;
import com.example.lmsbackend.multitenancy.domain.TenantEntity;
import com.example.lmsbackend.multitenancy.dto.GetPaymentURLRequest;
import com.example.lmsbackend.multitenancy.dto.VNPayPaymentResult;
import com.example.lmsbackend.multitenancy.dto.VNPayReturnResult;
import com.example.lmsbackend.multitenancy.enums.VNPayPaymentResultEnum;
import com.example.lmsbackend.multitenancy.exception.CannotCreatePaymentTransactionException;
import com.example.lmsbackend.multitenancy.exception.PackageNotExistsException;
import com.example.lmsbackend.multitenancy.payment_gateway.momo.MoMo;
import com.example.lmsbackend.multitenancy.payment_gateway.momo.dto.GetPaymentMethodResponse;
import com.example.lmsbackend.multitenancy.payment_gateway.vnpay.VnPay;
import com.example.lmsbackend.multitenancy.payment_gateway.vnpay.config.VNPayConfig;
import com.example.lmsbackend.multitenancy.repository.PackageRepository;
import com.example.lmsbackend.multitenancy.repository.TenantRepository;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.example.lmsbackend.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    public static final String TRANSACTION_KEY_PREFIX = "transaction_";
    private final MoMo moMo;
    private final VnPay vnPay;
    private final VNPayConfig vnPayConfig;
    private final RedissonClient redissonClient;
    private final PackageRepository packageRepository;
    private final TenantRepository tenantRepository;
    private final MailUtils mailUtils;
    @Autowired
    private HttpServletRequest request;

    public String payment(GetPaymentURLRequest request) {
        try {
            String transactionId;
            PackageEntity packageEntity = packageRepository.findById(request.getPackageId())
                    .orElseThrow(() -> {
                        throw new PackageNotExistsException();
                    });
            switch (request.getGateway()) {
                case MOMO:
                    GetPaymentMethodResponse getPaymentMethodResponse = moMo.getPaymentMethod(packageEntity.getPrice(), "tenantTest");
                    return getPaymentMethodResponse.getPayUrl();
                case VNPAY: {
                    transactionId = VNPayConfig.getRandomNumber(8);
                    RMapCache<String, Object> transactionMap = redissonClient.getMapCache(TRANSACTION_KEY_PREFIX + transactionId);
                    transactionMap.put("tenant", TenantContext.getTenantId(), 20, TimeUnit.MINUTES);
                    transactionMap.put("package", request.getPackageId(), 20, TimeUnit.MINUTES);
                    transactionMap.put("amount", packageEntity.getPrice(), 20, TimeUnit.MINUTES);
                    transactionMap.put("time", LocalDateTime.now(), 20, TimeUnit.MINUTES);
                    return vnPay.createPaymentURL(packageEntity.getPrice(), transactionId);
                }
            }
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CannotCreatePaymentTransactionException();
        }
        return null;
    }

    public VNPayReturnResult handleVNPayPaymentReturn(VNPayPaymentResult paymentResult) {
        try {
            //Begin process return from VNPAY
            String transID = paymentResult.getVnp_TxnRef();

            // check data in cache is exists
            RMapCache<String, Object> transactionMap = redissonClient.getMapCache(TRANSACTION_KEY_PREFIX + transID);
            if (!transactionMap.isExists()) {
                return new VNPayReturnResult(VNPayPaymentResultEnum.ORDER_NOT_FOUND);
            }

            // check amount
            Long packagePrice = (Long) transactionMap.get("amount");
            if (packagePrice != paymentResult.getVnp_Amount()/100) {
                return new VNPayReturnResult(VNPayPaymentResultEnum.INVALID_AMOUNT);
            }

            String tenant = (String) transactionMap.get("tenant");
            TenantEntity tenantEntity = tenantRepository.findByTenantId(tenant);

            if ("00".equals(paymentResult.getVnp_ResponseCode())) {
                // thanhf coong
                log.info("Thanh cong");
                Optional<PackageEntity> packageEntityOpt = packageRepository.findById((Integer) transactionMap.get("package"));
                if (packageEntityOpt.isEmpty()) {
                    return new VNPayReturnResult(VNPayPaymentResultEnum.INVALID_CHECKSUM);
                }
                PackageEntity packageEntity = packageEntityOpt.get();
                plusExpireTimeForTenant(tenantEntity, packageEntity);
                return new VNPayReturnResult(VNPayPaymentResultEnum.SUCCESS);
            } else {
                // thaast bai
                log.info("Chuyển tiền VNPay thất bại");
                LocalDateTime timeExt = (LocalDateTime) transactionMap.get("time");
                sendEmailNotiFail(tenantEntity, timeExt);
                return new VNPayReturnResult(VNPayPaymentResultEnum.SUCCESS);

            }

        } catch (Exception e) {
            return new VNPayReturnResult(VNPayPaymentResultEnum.UNKNOWN_ERROR);
        }
    }

    private void plusExpireTimeForTenant(TenantEntity tenantEntity, PackageEntity packageEntity) {
        LocalDateTime newExpireTime = tenantEntity.getExpireTime().plusMonths(packageEntity.getNumberOfMonths());
        tenantEntity.setExpireTime(newExpireTime);
        tenantRepository.save(tenantEntity);
    }

    private void sendEmailNotiFail(TenantEntity tenantEntity, LocalDateTime timeExcusePayment) {
        HashMap<String, Object> content = new HashMap<>();
        content.put("name", tenantEntity.getFirstname().concat(" ").concat(tenantEntity.getLastname()));
        content.put("time", timeExcusePayment.toString());
        mailUtils.sendMailWithTemplate(
                "BKLMS",
                tenantEntity.getEmail(),
                "BKLMS - Thông báo gia hạn dịch vụ thất bại",
                content,
                AppConstant.EXTEND_SERVICE_FAIL_NOTI
        );
    }
}
