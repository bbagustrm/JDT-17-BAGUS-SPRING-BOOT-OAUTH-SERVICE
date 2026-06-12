package com.jdt17.oauth.service.data.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OauthVariable {
    public static final String HEADER_NAME_USER_IP = "X-User-IP";
    public static final String HEADER_NAME_DEVICE_IP = "X-Device-FP";
    public static final String HEADER_NAME_USER_FINGERPRINT = "X-Fingerprint";
    public static final String HEADER_NAME_USER_AGENT = "X-User-Agent";
    public static final String HEADER_ACCEPTED_LANGUAGE = "Accept-Language";
    public static final String HEADER_NAME_USER_TIMEZONE = "X-Timezone";
    public static final String HEADER_NAME_USER_ID = "X-User-ID";
    public static final String HEADER_NAME_FCM_TOKEN = "X-FCM-Token";
    public static final String HEADER_NAME_DEVICE_ID = "X-Device-Id";
    public static final String HEADER_NAME_DEVICE_REAL = "X-Device-Real";
    public static final String HEADER_NAME_DEVICE_PLATFORM = "X-Device-Platform";
}
