package com.jdt17.oauth.service.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestWrapperDTO<T> {
    private T request;
    private String requestUsersId;
    private String requestRemoteIp;
    private String requestTimeZone;
    private String requestDeviceFingerprint;
    private String requestUserAgent;
    private String requestFcmToken;
    private String requestDeviceId;
    private String requestDeviceReal;
    private String requestDevicePlatform;
}
