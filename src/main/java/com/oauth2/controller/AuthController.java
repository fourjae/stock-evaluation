package com.oauth2.controller;

import com.oauth2.dto.KakaoLoginResponse;
import com.oauth2.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final OauthService oauthService;

    /**
     * 사용자로부터 인가코드를 받아 카카오 서버로부터 유저 정보를 받고 AccessToken 발행
     * @return 로그인 성공 유무 및 accessToken(JWT)
     */
    @GetMapping("/kakao/callback")
    public KakaoLoginResponse kakaoOauth(@RequestParam("code") String code)  {
//        String accessToken = oauthService.getKakaoOauthToken(code);

        return KakaoLoginResponse.builder()
                .successYn(true)
                .accessToken("accessToken")
                .build();
    }


//    /**
//     * 애플 유저 로그인 후 정보 받기
//     * @return 로그인 성공 유무 및 accessToken(JWT)
//     */
//    @PostMapping("/apple/callback")
//    public boolean AppleOauth(AppleResponse appleResponse)  {
//        return oauthService.isAppleValidations(appleResponse);
//    }
//    public TokenResponse servicesRedirect(ServicesResponse serviceResponse) {
//
//        if (serviceResponse == null) {
//            return null;
//        }
//
//        String code = serviceResponse.getCode();
//        String client_secret = appleService.getAppleClientSecret(serviceResponse.getId_token());
//
//        logger.debug("================================");
//        logger.debug("id_token ‣ " + serviceResponse.getId_token());
//        logger.debug("payload ‣ " + appleService.getPayload(serviceResponse.getId_token()));
//        logger.debug("client_secret ‣ " + client_secret);
//        logger.debug("================================");
//
//        return appleService.requestCodeValidations(client_secret, code, null);
//    }






//    @GetMapping("/test")
//    public Boolean Test(HttpServletRequest request){
//        String jwtToken = jwtTokenProvider.getTokenFromHeader(request);
//        return jwtTokenProvider.validateToken(jwtToken);
//    }
//
//
//    /**
//     * 사용자로부터 인가코드를 받아 구글 서버로부터 유저 정보를 받고 AccessToken 발행(
//     * @return 로그인 성공 유무 및 accessToken(JWT)
//     */
////    @Deprecated
////    @GetMapping("/google/callback")
////    public GoogleLoginResponse googleOauth(@RequestParam("code") String code)  {
////        String accessToken = authService.getGoogleOauthToken(code);
////        return new GoogleLoginResponse(true, accessToken);
////    }
//
//    /**
//     * 구글 서버로부터 인증(firebase 기반)된 사용자 정보를 받아 유저 정보에 대한 AccessToken 발행
//     * @param googleRequestOauthDto
//     * @return 로그인 성공 유무 및 accessToken(JWT)
//     */
//    @PostMapping("/google/callback")
//    public GoogleLoginResponse googleOauth(@RequestBody GoogleRequestOauthDto googleRequestOauthDto)  {
//        String accessToken = authService.googleLogin(googleRequestOauthDto);
//        return new GoogleLoginResponse(true, accessToken);
//    }
//


}
