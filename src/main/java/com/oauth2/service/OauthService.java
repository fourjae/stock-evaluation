package com.oauth2.service;

import com.oauth2.model.AppleResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OauthService {

    public boolean isAppleLoginData(AppleResponse appleResponse) {
        if (appleResponse == null) {
            return false;
        }
        return true;
    }

    public String getKakaoOauthToken(String code) {
        return code;
    }
}
