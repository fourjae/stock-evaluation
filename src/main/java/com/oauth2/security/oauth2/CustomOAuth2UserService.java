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

import static com.oauth2.config.constants.SecurityConstants.*;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User user = delegate.loadUser(req);
        String registrationId = req.getClientRegistration().getRegistrationId();

        Map<String, Object> flat = switch (registrationId) {
            case KAKAO  -> flattenKakao(user.getAttributes());
            case NAVER  -> flattenNaver(user.getAttributes());
            case GOOGLE -> user.getAttributes();          // 이미 평평
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

        kakao.put(ID,        src.get(ID));
        kakao.put(EMAIL,     account.get(EMAIL));
        kakao.put(NAME,      profile.get(NAME));
        kakao.put(PICTURE,   profile.get("profile_image_url"));

        return kakao;
    }

    private Map<String, Object> flattenNaver(Map<String, Object> src) {
        Map<String, Object> naver = new LinkedHashMap<>();   // 순서 유지

        // Naver 응답은 {"resultcode":"00","message":"success","response":{...}} 구조
        Map<String, Object> response = (Map<String, Object>) src.get("response");
        if (Objects.isNull(response)) {
            throw new OAuth2AuthenticationException("Invalid Naver response");
        }

        naver.put(ID,      response.get(ID));
        naver.put(EMAIL,   response.get(EMAIL));
        naver.put(NAME,    response.get(NAME));
        naver.put(PICTURE, response.get("profile_image"));  // 통일된 키

        return naver;
    }
}

//