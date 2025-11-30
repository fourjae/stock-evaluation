package com.oauth2.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Builder
public class CustomUserPrincipal implements OAuth2User, UserDetails {

    // 도메인 User의 내부 관리용 ID
    private final String userId;

    // 로그인 아이디로 쓸 이메일
    private final String email;

    // 화면 표시용 이름 (닉네임 등)
    private final String name;

    // 권한들
    private final Collection<? extends GrantedAuthority> authorities;

    // OAuth2 attributes (필요 없으면 Map.of() 정도로 비워두면 됨)
    private final Map<String, Object> attributes;

    // ========================
    // 편의 메서드
    // ========================

    public static CustomUserPrincipal of(
            String userId,
            String email,
            String name,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes
    ) {
        return CustomUserPrincipal.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .authorities(authorities)
                .attributes(attributes)
                .build();
    }

    // ========================
    // OAuth2User 구현
    // ========================

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * OAuth2User.getName()
     * - 기본 이름 리턴 (로그 등에서 사용)
     */
    @Override
    public String getName() {
        return name != null ? name : email;
    }

    // ========================
    // UserDetails 구현
    // ========================

    @Override
    public String getUsername() {
        return email;
    }

    // 패스워드는 OAuth2만 쓸 거면 의미 없어서 빈 값 리턴
    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
