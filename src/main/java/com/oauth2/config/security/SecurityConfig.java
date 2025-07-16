package com.oauth2.config.security;

import com.oauth2.security.oauth2.CustomOAuth2UserService;
import com.oauth2.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.oauth2.security.oauth2.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.core.*;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler successHandler;

    /* ─────────────── ① 필터 체인 ─────────────── */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")                    // 커스텀 로그인 페이지
                        .userInfoEndpoint(u -> u
                                .userService(oAuth2UserService) // Kakao attribute 매핑
                        )
                        .successHandler(successHandler)         // JWT 발급
                );

        return http.build();
    }


    /* ─────────────── ② Kakao + Google 등록 ─────────────── */
    @Bean
    public ClientRegistrationRepository clientRegistrations(OAuth2ClientProperties props) {

        // Kakao
        var kr = props.getRegistration().get("kakao");
        var kp = props.getProvider()    .get("kakao");

        ClientRegistration kakao = ClientRegistration
                .withRegistrationId("kakao")
                .clientId(kr.getClientId())
                .clientSecret(kr.getClientSecret())
                .clientAuthenticationMethod(
                        new ClientAuthenticationMethod(kr.getClientAuthenticationMethod()))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(kr.getRedirectUri())
                .scope(kr.getScope())
                .authorizationUri(kp.getAuthorizationUri())
                .tokenUri(kp.getTokenUri())
                .userInfoUri(kp.getUserInfoUri())
                .userNameAttributeName(kp.getUserNameAttribute())
                .build();

        // Google (Spring 내장 빌더)
        var gr = props.getRegistration().get("google");
        ClientRegistration google = CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId(gr.getClientId())
                .clientSecret(gr.getClientSecret())
                .scope(gr.getScope())
                .redirectUri(gr.getRedirectUri())
                .build();

        return new InMemoryClientRegistrationRepository(kakao, google);
    }

    /* ─────────────── ③ WebClient – Kakao 호출용(선택) ─────────────── */
    @Bean
    public WebClient kakaoWebClient(ClientRegistrationRepository repo) {

        // 토큰 갱신·첨부까지 자동 처리
        OAuth2AuthorizedClientService svc = new InMemoryOAuth2AuthorizedClientService(repo);
        OAuth2AuthorizedClientManager mgr =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(repo, svc);

        var oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(mgr);
        oauth2.setDefaultClientRegistrationId("kakao");

        return WebClient.builder()
                .apply(oauth2.oauth2Configuration())
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
