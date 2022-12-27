package com.oauth2.model;

import lombok.Getter;

@Getter
public class AppleResponse {

    private String state;
    private String code;
    private String id_token;
    private String user;

}

