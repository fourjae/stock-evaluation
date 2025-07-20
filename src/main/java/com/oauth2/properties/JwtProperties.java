package com.oauth2.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret, /** 256-bit(32바이트) 이상 Base64 인코딩 권장 */
        long expirationMillis /** 만료 시간(ms). 기본 4h */
) {

}