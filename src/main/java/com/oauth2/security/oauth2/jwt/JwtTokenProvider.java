package com.oauth2.security.oauth2.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.*;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties props;     // ← 설정 빈 주입

    private Key key() {
        return Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String subject, Collection<? extends GrantedAuthority> auth) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", auth.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setExpiration(new Date(System.currentTimeMillis() + props.getExpirationMillis()))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication parse(String token) {
        Claims c = Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody();
        List<GrantedAuthority> roles = ((List<?>) c.get("roles")).stream()
                .map(r -> new SimpleGrantedAuthority((String) r))
                .toList();
        return new UsernamePasswordAuthenticationToken(c.getSubject(), null, roles);
    }
}
