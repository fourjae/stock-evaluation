package com.oauth2.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** 256-bit(32바이트) 이상 Base64 인코딩 권장 */
    private String secret;

    /** 만료 시간(ms). 기본 4h */
    private long  expirationMillis = 14_400_000L;
}