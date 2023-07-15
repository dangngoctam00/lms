package com.example.lmsbackend.multitenancy.service;

import com.example.lmsbackend.client.google.GoogleDNSClient;
import com.example.lmsbackend.config.AppConfig;
import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.jwt.JWTUtils;
import com.example.lmsbackend.config.web.FlywayConfiguration;
import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.domain.TenantConfigEntity;
import com.example.lmsbackend.domain.event.CalendarType;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.exceptions.EmailUsedException;
import com.example.lmsbackend.multitenancy.domain.TenantCustomizeEntity;
import com.example.lmsbackend.multitenancy.domain.TenantEntity;
import com.example.lmsbackend.multitenancy.dto.*;
import com.example.lmsbackend.multitenancy.exception.*;
import com.example.lmsbackend.multitenancy.mapper.TenantMapper;
import com.example.lmsbackend.multitenancy.repository.TenantCustomizeRepository;
import com.example.lmsbackend.multitenancy.repository.TenantRepository;
import com.example.lmsbackend.multitenancy.utils.TenantContext;
import com.example.lmsbackend.repository.StaffRepository;
import com.example.lmsbackend.repository.UserRepository;
import com.example.lmsbackend.service.EventService;
import com.example.lmsbackend.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantService {
    public static final String TENANT_PREFIX = "Tenant_";
    public static final String VERIFY_CODE_PREFIX = "VerifyCode_";
    private final TenantRepository tenantRepository;

    private final TenantCustomizeRepository tenantCustomizeRepository;
    private final TenantMapper mapper;
    private final FlywayConfiguration flywayConfiguration;
    private final GoogleDNSClient dnsClient;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventService eventService;
    private final EntityManager entityManager;
    private final RedissonClient redissonClient;
    private final MailUtils mailUtils;
    private final AppConfig appConfig;
    private final JWTUtils jwtUtils;

    @Transactional
    public String createTenant(String verifyCode) {
        RBucket<String> verifyCodeBucket = redissonClient.getBucket(VERIFY_CODE_PREFIX + verifyCode);
        String domainName = verifyCodeBucket.get();
        if (domainName != null) {
            RBucket<TenantDto> tenantDtoRBucket = redissonClient.getBucket(TENANT_PREFIX + domainName);
            TenantDto dto = tenantDtoRBucket.get();
            if (dto != null) {
                verifyCodeBucket.delete();
                tenantDtoRBucket.delete();

                createSubdomain(dto);
                createDatabase(dto);
                addUser(dto);
                return dto.getDomain();
            } else {
                throw new VerifyUrlInvalidException();
            }
        } else {
            throw new VerifyUrlInvalidException();
        }
    }

    private void addUser(TenantDto dto) {
        TenantContext.setTenantId(dto.getDomain());
        entityManager.createNativeQuery("SET search_path TO " + dto.getDomain()).executeUpdate();

        StaffEntity user = StaffEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .avatar("https://haycafe.vn/wp-content/uploads/2021/11/Avatar-gau-tiktok-de-thuong.jpg")
                .firstName(dto.getFirstname())
                .lastName(dto.getLastname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .accountType(AccountTypeEnum.STAFF)
                .createdAt(LocalDateTime.now())
                .build();
        user = staffRepository.save(user);
        eventService.createCalendar(CalendarType.USER, user.getId());
    }

    private synchronized void createDatabase(TenantDto dto) {
        var tenant = mapper.mapToTenant(dto);
        tenant.setExpireTime(LocalDateTime.now().plusMonths(1));
        tenant.setPassword(passwordEncoder.encode(tenant.getPassword()));
        tenantRepository.save(tenant);
        TenantCustomizeEntity tenantCustomizeEntity = new TenantCustomizeEntity();
        tenantCustomizeEntity.setTenantId(tenant.getTenantId());
        tenantCustomizeEntity.setName(tenant.getTenantId());
        tenantCustomizeEntity.setLogo("https://cdn.haitrieu.com/wp-content/uploads/2021/09/Logo-DH-Bach-Khoa-HCMUT.png");
        tenantCustomizeEntity.setBanner("https://i.pinimg.com/736x/56/86/03/568603cbd1860c67bf8f6776cbe7f885.jpg");
        tenantCustomizeRepository.save(tenantCustomizeEntity);
        flywayConfiguration.migrate(dto);
    }

    private void createSubdomain(TenantDto dto) {
        boolean isSuccess = dnsClient.createNewSubdomain(dto.getDomain().trim());
        if (!isSuccess) {
            log.info("Can not create domain '{}'", dto.getDomain());
            throw new CannotCreateDomainException();
        }
    }

    private boolean isExistedTenant(TenantDto dto) {
        var tenantOpt = tenantRepository.findById(dto.getDomain());
        if (tenantOpt.isPresent()) {
            log.info("Domain '{}' is already used", dto.getDomain());
            return true;
        }
        if (dnsClient.checkExisted(dto.getDomain())) {
            log.info("Domain '{}' is already used", dto.getDomain());
            return true;
        }
        RBucket<TenantDto> tenantDtoRBucket = redissonClient.getBucket(TENANT_PREFIX + dto.getDomain());
        return tenantDtoRBucket.get() != null;
    }

    public String createTenantCache(TenantDto dto) {
        Optional<TenantEntity> tenantEntityOptional = tenantRepository.findByEmail(dto.getEmail());
        if (tenantEntityOptional.isPresent()){
            throw new EmailUsedException();
        }
        if (isExistedTenant(dto)) {
            throw new DomainAlreadyExistsException(dto.getDomain());
        }
        UUID verifyCode = UUID.randomUUID();

        RBucket<String> verifyCodeBucket = redissonClient.getBucket(VERIFY_CODE_PREFIX + verifyCode);
        verifyCodeBucket.set(dto.getDomain(), 7, TimeUnit.DAYS);

        RBucket<TenantDto> tenantDtoRBucket = redissonClient.getBucket(TENANT_PREFIX + dto.getDomain());
        tenantDtoRBucket.set(dto, 7, TimeUnit.DAYS);

        String mailSubject = "Chào mừng bạn đến với hệ thống BKLMS";
        String mailContent = generateMailContent(dto, verifyCode.toString());
        mailUtils.sendMail("BKLMS", dto.getEmail(), mailSubject, mailContent);
        return dto.getDomain();
    }

    private String generateMailContent(TenantDto dto, String verifyCode) {
        log.info(InetAddress.getLoopbackAddress().getHostName());
        String verifyUrl = "http://system.".concat(appConfig.getDomain()).concat("/api/v1/tenant/").concat(verifyCode);
        log.info("VerifyUrl: {}", verifyUrl);
        return "<p>Chào ".concat(dto.getFirstname()).concat(",</p>")
                .concat("<p>Cảm ơn bạn đã tin tưởng và đăng ký sử dụng dịch vụ của chúng tôi.</p>")
                .concat("<p>Vui lòng truy cập vào <a href='").concat(verifyUrl).concat("'>đây</a> để hoàn tất quá trình đăng ký.</p>")
                .concat("<p>Chúc bạn học tập tốt.</p>");
    }

    public TenantLoginResponse login(LoginRequest request) {
        Optional<TenantEntity> tenantEntityOptional = tenantRepository.findByUsername(request.getUsername());
        if (tenantEntityOptional.isPresent()) {
            TenantEntity tenantEntity = tenantEntityOptional.get();
            if (passwordEncoder.matches(request.getPassword(), tenantEntity.getPassword())) {
                UserDto userDto = mapper.mapToUserDto(tenantEntity);
                CustomUserDetails userDetails = new CustomUserDetails(1L, request.getUsername(), AccountTypeEnum.STAFF);
                return TenantLoginResponse.builder()
                        .tenant(tenantEntity.getTenantId())
                        .token(jwtUtils.generateToken(userDetails, tenantEntity.getTenantId()))
                        .refreshToken(jwtUtils.generateRefreshToken(userDetails))
                        .user(userDto)
                        .build();

            } else {
                throw new IncorrectPasswordException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }


    public LocalDateTime getExpireTime() {
        Optional<TenantEntity> tenantEntityOptional = tenantRepository.findById(TenantContext.getTenantId());
        if (tenantEntityOptional.isPresent()){
            return tenantEntityOptional.get().getExpireTime();
        }
        else {
            throw new TenantNotFoundException(TenantContext.getTenantId());
        }
    }

    public void customTenant(CustomTenantInfo customTenantRequest) {
        TenantCustomizeEntity tenantCustomizeEntity = tenantCustomizeRepository.findById(customTenantRequest.getTenantId())
                .orElseThrow(()->new TenantNotFoundException(customTenantRequest.getTenantId()));
        tenantCustomizeEntity.setName(customTenantRequest.getName());
        tenantCustomizeEntity.setLogo(customTenantRequest.getLogo());
        tenantCustomizeEntity.setBanner(customTenantRequest.getBanner());

        tenantCustomizeRepository.save(tenantCustomizeEntity);
    }


    public CustomTenantInfo getCustomTenantInfo(String tenantId) {
        TenantCustomizeEntity tenantCustomizeEntity = tenantCustomizeRepository.findById(tenantId)
                .orElseThrow(()->new TenantNotFoundException(tenantId));
        CustomTenantInfo customTenantInfo = new CustomTenantInfo();
        customTenantInfo.setTenantId(tenantCustomizeEntity.getTenantId());
        customTenantInfo.setName(tenantCustomizeEntity.getName());
        customTenantInfo.setLogo(tenantCustomizeEntity.getLogo());
        customTenantInfo.setBanner(tenantCustomizeEntity.getBanner());
        return customTenantInfo;
    }
}
