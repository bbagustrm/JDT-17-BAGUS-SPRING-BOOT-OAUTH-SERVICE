package com.jdt17.oauth.service.controller.module;

import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.data.request.RequestWrapperDTO;
import com.jdt17.oauth.service.data.response.RestApiPath;
import com.jdt17.oauth.service.data.response.RestApiResponse;
import com.jdt17.oauth.service.data.response.module.OauthLoginResponse;
import com.jdt17.oauth.service.service.module.ProfileInterface;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.jdt17.oauth.service.data.utility.OauthVariable.*;
import static com.jdt17.oauth.service.data.utility.OauthVariable.HEADER_NAME_DEVICE_ID;
import static com.jdt17.oauth.service.data.utility.OauthVariable.HEADER_NAME_DEVICE_PLATFORM;
import static com.jdt17.oauth.service.data.utility.OauthVariable.HEADER_NAME_DEVICE_REAL;
import static com.jdt17.oauth.service.data.utility.OauthVariable.HEADER_NAME_USER_AGENT;
import static com.jdt17.oauth.service.data.utility.OauthVariable.HEADER_NAME_USER_FINGERPRINT;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestApiPath.BASE_PATH + RestApiPath.OAUTH_BASE_PATH)
@Slf4j
public class ProfileController {

    private final ProfileInterface profileInterface;

    /**
     * Handles user login request.
     * <p>
     * This endpoint authenticates a user using their username and password.
     * It also supports additional headers for IP, timezone, user agent, and device fingerprint for logging and tracking.
     * </p>
     *
     * @param remoteIP The IP address of the client making the request.
     * @param remoteTimeZone The timezone of the client making the request.
     * @param userAgent The user agent of the client making the request.
     * @param deviceFingerprint The fingerprint of the client's device.
     * @return The OAuth login result wrapped in a RestApiResponse.
     * @throws CoreThrowHandler If there is an error during the login process.
     */
    @GetMapping(RestApiPath.OAUTH_PROFILE_PATH)
    public RestApiResponse<OauthLoginResponse> getProfile(
            @RequestHeader(value = HEADER_NAME_USER_ID, required = true) String userId,
            @RequestHeader(value = HEADER_NAME_USER_IP, required = false) String remoteIP,
            @RequestHeader(value = HEADER_NAME_USER_TIMEZONE, required = false) String remoteTimeZone,
            @RequestHeader(value = HEADER_NAME_FCM_TOKEN, required = false) String fcmToken,
            @RequestHeader(value = HEADER_NAME_DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = HEADER_NAME_DEVICE_REAL, required = false) String deviceReal,
            @RequestHeader(value = HEADER_NAME_DEVICE_PLATFORM, required = false) String devicePlatform,
            @RequestHeader(value = HEADER_NAME_USER_AGENT, required = false) String userAgent,
            @RequestHeader(value = HEADER_NAME_USER_FINGERPRINT, required = false) String deviceFingerprint
    ) throws CoreThrowHandler {
        RequestWrapperDTO<Object> requestWrapperDTO = new RequestWrapperDTO<>(
                null,
                userId,
                remoteIP,
                remoteTimeZone,
                userAgent,
                deviceFingerprint,
                fcmToken,
                deviceId,
                deviceReal,
                devicePlatform
        );

        return profileInterface.getProfile(requestWrapperDTO);
    }
}
