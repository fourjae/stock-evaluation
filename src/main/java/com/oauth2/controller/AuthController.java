package com.oauth2.controller;

import com.oauth2.model.AppleResponse;
import com.oauth2.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final OauthService oauthService;
    public AuthController(OauthService oauthService){
        this.oauthService=oauthService;
    }

    /**
     * 사용자로부터 인가코드를 받아 카카오 서버로부터 유저 정보를 받고 AccessToken 발행
     * @return 로그인 성공 유무 및 accessToken(JWT)
     */
    @GetMapping("/kakao/callback")
    public KakaoLoginResponse kakaoOauth(@RequestParam("code") String code)  {
        String accessToken = authService.getKakaoOauthToken(code);
        return new KakaoLoginResponse(true, accessToken);
    }


    /**
     * 애플 유저 로그인 후 정보 받기
     * @return 로그인 성공 유무 및 accessToken(JWT)
     */
    @PostMapping("/apple/callback")
    @ResponseBody
    public boolean AppleOauth(AppleResponse appleResponse)  {
        return oauthService.isAppleValidations(appleResponse);
        return true;
    }

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
//    /**
//     * 애플 서버로부터 인증(firebase 기반)된 사용자 정보를 받아 유저 정보에 대한 AccessToken 발행
//     * @param googleRequestOauthDto
//     * @return 로그인 성공 유무 및 accessToken(JWT)
//     */
//    @PostMapping("/apple/callback")
//    public GoogleLoginResponse appleOauth(@RequestBody GoogleRequestOauthDto googleRequestOauthDto)  {
//        String accessToken = authService.appleLogin(googleRequestOauthDto);
//        return new GoogleLoginResponse(true, accessToken);
//    }

}
