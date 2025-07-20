package com.oauth2.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Oauth2에 대한 로그인을 담당하는 서비스
 * */
@Service
@Transactional
public class OauthService {
//    public boolean isAppleLoginData(AppleResponse appleResponse) {
//        if (appleResponse == null) {
//            return false;
//        }
//        return true;
//    }

//    /**
//     **
//     * 사용자로부터 인가 CODE를 받아서 TOKEN을 요청하는 함수
//     * @param code 토큰 받기 요청에 필요한 인가 코드
//     * @return access-Token
//     */
//    public String getKakaoOauthToken(String code) {
//        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
//
//        data.add("grant_type", "authorization_code");
//        data.add("client_id", KAKAO_OAUTH_API_KEY);
//        data.add("redirect_uri", KAKAO_OAUTH_REDIRECT_URI);
//        data.add("code", code);
//
//        URI uri = UriComponentsBuilder
//                .fromUriString(KAKAO_OAUTH_TOKEN_API_URI)
//                .encode()
//                .build()
//                .toUri();
//        ResponseEntity<GetKakaoOauthTokenResponseDto> result = restTemplate.postForEntity(uri, data,
//                GetKakaoOauthTokenResponseDto.class);
//
//        if (result.getStatusCode() != HttpStatus.OK) {
//            return null;
//        }
//        GetKakaoOauthTokenResponseDto token_getbody = result.getBody();
//        if (token_getbody == null) return null;
//
//        String token = token_getbody.getAccess_token();
//
//        return kakaoLogin(token);
//    }
//
//    public KakaoLoginResponse loginWithCode(String code) {
//        try {
//            String accessToken = requestAccessToken(code);
//            return kakaoLogin(accessToken);
//        } catch (RestClientResponseException ex) {
//            log.error("Kakao OAuth token request failed: {} – {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
//            throw new IllegalStateException("카카오 인증에 실패했습니다.", ex);
//        }
//    }
//
//    private String requestAccessToken(String code) {
//        URI uri = UriComponentsBuilder
//                .fromUriString(KAKAO_OAUTH_TOKEN_API_URI)
//                .build()
//                .toUri();
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(
//                buildFormData(code),
//                buildHeaders()
//        );
//
//        GetKakaoOauthTokenResponseDto body = restTemplate
//                .postForObject(uri, request, GetKakaoOauthTokenResponseDto.class);
//
//        if (body == null || !StringUtils.hasText(body.getAccess_token())) {
//            throw new IllegalStateException("카카오에서 토큰을 받지 못했습니다.");
//        }
//        return body.getAccess_token();
//    }
//
//    private MultiValueMap<String, String> buildFormData(String code) {
//        LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();
//        data.add("grant_type", GRANT_TYPE);
//        data.add("client_id", KAKAO_OAUTH_API_KEY);
//        data.add("redirect_uri", KAKAO_OAUTH_REDIRECT_URI);
//        data.add("code", code);
//        return data;
//    }
//
//    private HttpHeaders buildHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        return headers;
//    }
//
//    private KakaoLoginResponse kakaoLogin(String accessToken) {
//        // accessToken으로 Kakao 사용자 정보 조회 후 JWT 발급 구현
//        …
//    }
}