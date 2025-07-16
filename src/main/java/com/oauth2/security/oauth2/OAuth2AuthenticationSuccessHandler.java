package com.oauth2.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        String jwt = jwtTokenProvider.createToken(oauthUser.getName(), oauthUser.getAuthorities());

        // ① 헤더에 실어 보내기
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        // ② 혹은 쿠키·JSON 바디·리다이렉트 파라미터 등으로 전달
        response.sendRedirect("/login/success");   // 프론트 전용 페이지
    }
}
