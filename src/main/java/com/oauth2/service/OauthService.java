package com.oauth2.service;

import com.oauth2.model.AppleResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.DispatcherServlet;
import java.util.HashMap;

public class OauthService {
    @Transactional
    public boolean isAppleLoginData(AppleResponse appleResponse){
        HashMap<Integer,Integer> hashMap = new HashMap<Integer,Integer>;
        if (appleResponse ==null){
            return false;
        }
        return true;
    }

}
