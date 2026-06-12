package com.jdt17.oauth.service.data.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestApiPath {
    public static final String BASE_PATH = "/api/v1";
    public static final String OAUTH_BASE_PATH = "/oauth";

    public static final String OAUTH_LOGIN_PATH = "/login";
    public static final String OAUTH_PROFILE_PATH = "/profile";
    public static final String OAUTH_REGISTER_PATH = "/register";

    public static final String SERVICE_OAUTH_MAIN = "/api/v1/oauth/**";
}