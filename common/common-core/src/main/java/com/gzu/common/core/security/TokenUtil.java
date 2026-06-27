package com.gzu.common.core.security;

import com.gzu.common.core.exception.TokenException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

public final class TokenUtil {
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private TokenUtil() {
    }

    /**
     * 生成JWT Token。使用HmacSHA256签名算法，格式为: encoded_payload.signature
     * @param userId 用户ID
     * @param username 用户名
     * @param role 用户角色
     * @param secret 签名密钥（使用app.security.secret配置）
     * @param ttl Token有效期（使用app.security.ttl-minutes配置）
     * @return Token字符串，可直接用于Authorization请求头
     */
    public static String createToken(Long userId, String username, String role, String secret, Duration ttl) {
        Instant now = Instant.now();
        TokenPayload payload = new TokenPayload(userId, username, role, now.toEpochMilli(), now.plus(ttl).toEpochMilli());
        String raw = encodePayload(payload);
        return raw + "." + sign(raw, secret);
    }

    /**
     * 验证Token的合法性。检查签名和过期时间
     * @param token 要验证的Token字符串
     * @param secret 签名密钥（必须与生成Token时使用的secret一致）
     * @return Optional<TokenPayload>，验证成功返回payload，失败返回空Optional
     */
    public static Optional<TokenPayload> verify(String token, String secret) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        String[] parts = token.split("\\.", 2);
        if (parts.length != 2) {
            return Optional.empty();
        }
        String raw = parts[0];
        String expected = sign(raw, secret);
        if (!Objects.equals(expected, parts[1])) {
            return Optional.empty();
        }
        TokenPayload payload = decodePayload(raw);
        if (payload.expiresAt() < Instant.now().toEpochMilli()) {
            return Optional.empty();
        }
        return Optional.of(payload);
    }

    /**
     * 验证Token并返回payload，验证失败直接抛出TokenException异常
     * @param token 要验证的Token字符串
     * @param secret 签名密钥
     * @return TokenPayload 包含userId、username、role、issuedAt、expiresAt
     * @throws TokenException 当token无效或已过期时抛出
     */
    public static TokenPayload requireValid(String token, String secret) {
        return verify(token, secret).orElseThrow(() -> new TokenException("invalid token"));
    }

    private static String encodePayload(TokenPayload payload) {
        String raw = String.join("|",
                encodeField(String.valueOf(payload.userId())),
                encodeField(payload.username()),
                encodeField(payload.role()),
                encodeField(String.valueOf(payload.issuedAt())),
                encodeField(String.valueOf(payload.expiresAt())));
        return ENCODER.encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    private static TokenPayload decodePayload(String raw) {
        String decoded = new String(DECODER.decode(raw), StandardCharsets.UTF_8);
        String[] parts = decoded.split("\\|", -1);
        if (parts.length != 5) {
            throw new TokenException("invalid token payload");
        }
        return new TokenPayload(
                Long.parseLong(decodeField(parts[0])),
                decodeField(parts[1]),
                decodeField(parts[2]),
                Long.parseLong(decodeField(parts[3])),
                Long.parseLong(decodeField(parts[4])));
    }

    private static String encodeField(String value) {
        return ENCODER.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodeField(String value) {
        return new String(DECODER.decode(value), StandardCharsets.UTF_8);
    }

    private static String sign(String raw, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return ENCODER.encodeToString(mac.doFinal(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("failed to sign token", ex);
        }
    }
}

