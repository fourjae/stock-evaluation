package com.oauth2.security.oauth2.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.stream.*;

import com.oauth2.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;     // ← 설정 빈 주입
    private final SecretKey key;
    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret()));
    }


    /**
     * JWT 발급
     */
    public String createToken(String subject,
                              Collection<? extends GrantedAuthority> authorities) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(subject)
                .claim("roles", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtProperties.expirationMillis())))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }


    /**
     * 토큰 → Spring Authentication
     */
    public Authentication parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)   // header.payload.signature
                .getPayload();

        List<GrantedAuthority> roles = ((List<?>) claims.get("roles")).stream()
                .map(name -> (GrantedAuthority) new SimpleGrantedAuthority((String) name))
                .toList();

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, roles);
    }
}
