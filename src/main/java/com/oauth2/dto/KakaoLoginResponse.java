package com.oauth2.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class KakaoLoginResponse extends OauthLoginResponse {
    private boolean successYn;
}
