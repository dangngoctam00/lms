package com.example.lmsbackend.config.security.jwt;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.dto.request.file.CreateShareFileTokenRequest;
import com.example.lmsbackend.enums.AccountTypeEnum;
import com.example.lmsbackend.exceptions.jwt.JwtInvalidException;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class JWTUtils {

    @Autowired
    public JWTUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private final ResourceLoader resourceLoader;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private static final long JWT_EXPIRATION = 86400000L; // 1 day
    // * Reduce token valid times for expiration test - Thinh
    // private static final long JWT_EXPIRATION = 36000L; // 30p
    private static final long REFRESH_JWT_EXPIRATION = 604800000L; // 7 ngày
    private static final String USERNAME_CLAIM = "username";
    private static final String USER_ID_CLAIM = "userId";
    private static final String TENANT_ID_CLAIM = "tenantId";
    private static final String FILES_CLAIM = "files";
    private static final String ACCOUNT_TYPE = "accountType";


    @PostConstruct
    public void init() {
        publicKey = readKey(
                "classpath:key/public_key.pem",
                "PUBLIC",
                this::publicKeySpec,
                this::publicKeyGenerator
        );
        privateKey = readKey(
                "classpath:key/private_key.pem",
                "PRIVATE",
                this::privateKeySpec,
                this::privateKeyGenerator
        );
    }

    public String generateToken(CustomUserDetails userDetails, String tenantId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .claim(USERNAME_CLAIM, userDetails.getUsername())
                .claim(USER_ID_CLAIM, userDetails.getId())
                .claim(TENANT_ID_CLAIM, tenantId)
                .claim(ACCOUNT_TYPE, userDetails.getAccountType().name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_JWT_EXPIRATION);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .claim(USERNAME_CLAIM, userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String getUsername(String token) {
        validateToken(token);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(USERNAME_CLAIM, String.class);
    }

    public Long getUserId(String token) {
        validateToken(token);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(USER_ID_CLAIM, Long.class);
    }


    public String getTenantId(String token) {
        validateToken(token);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(TENANT_ID_CLAIM, String.class);
    }

    public AccountTypeEnum getAccountType(String token) {
        validateToken(token);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return AccountTypeEnum.valueOf(claims.get(ACCOUNT_TYPE, String.class));
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            log.info("Token {} has expired", token);
            throw new com.example.lmsbackend.exceptions.jwt.ExpiredJwtException(token, ex);
        } catch (UnsupportedJwtException
                | MalformedJwtException
                | SignatureException
                | IllegalArgumentException ex) {
            log.info("Token {} is invalid", token);
            throw new JwtInvalidException(token, ex);
        }
    }

    private <T extends Key> T readKey(String resourcePath, String headerSpec, Function<String, EncodedKeySpec> keySpec, BiFunction<KeyFactory, EncodedKeySpec, T> keyGenerator) {
        try {
            String keyString = asString(resourcePath);

            keyString = keyString.replace("-----BEGIN " + headerSpec + " KEY-----", "");
            keyString = keyString.replace("-----END " + headerSpec + " KEY-----", "");
            keyString = keyString.replaceAll("\\s+", "");

            return keyGenerator.apply(KeyFactory.getInstance("RSA"), keySpec.apply(keyString));
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new JwtInitializationException(e);
        }
    }

    private EncodedKeySpec privateKeySpec(String data) {
        return new PKCS8EncodedKeySpec(decode(data));
    }

    private EncodedKeySpec publicKeySpec(String data) {
        return new X509EncodedKeySpec(decode(data));
    }

    private PrivateKey privateKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePrivate(spec);
        } catch (InvalidKeySpecException e) {
            throw new JwtInitializationException(e);
        }
    }

    private PublicKey publicKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            throw new JwtInitializationException(e);
        }
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    private String asString(String resourcePath) throws IOException {
        Resource resource = resourceLoader.getResource(resourcePath);
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }

    public String generateShareFileToken(CreateShareFileTokenRequest request) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        Gson gson = new Gson();

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .claim(FILES_CLAIM, gson.toJson(request))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public List<String> getFilePaths(String token) {
        validateToken(token);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Gson gson = new Gson();
        return gson.fromJson(claims.get(FILES_CLAIM, String.class), CreateShareFileTokenRequest.class).getFullpaths();
    }
}
