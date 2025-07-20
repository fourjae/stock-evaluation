package com.oauth2.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User user = delegate.loadUser(req);
        String registrationId = req.getClientRegistration().getRegistrationId();

        Map<String, Object> flat = switch (registrationId) {
            case "kakao"  -> flattenKakao(user.getAttributes());
            case "google" -> user.getAttributes();          // 이미 평평
            default       -> throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        };
        // Google 은 기본 필드(id, name, email, picture)가 이미 매핑돼 있음.

        return new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_USER")),
                flat,
                "id"     // nameAttributeKey
        );
    }


    private Map<String, Object> flattenKakao(Map<String, Object> src) {
        Map<String, Object> kakao = new LinkedHashMap<>();   // 순서 유지

        Map<String, Object> account = (Map<String, Object>) src.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        kakao.put("id",        src.get("id"));
        kakao.put("email",     account.get("email"));
        kakao.put("name",      profile.get("nickname"));
        kakao.put("picture",   profile.get("profile_image_url"));

        return kakao;
    }
}

//