package com.oauth2.service;

import com.oauth2.model.AppleResponse;

public class OauthService {
    public boolean isAppleLoginData(AppleResponse appleResponse){
        if (appleResponse ==null){
            return false;
        }
        return true;
    }

}
