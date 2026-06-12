package com.jdt17.oauth.service.controller.module;


import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.data.request.OauthLoginRequest;
import com.jdt17.oauth.service.data.request.RequestWrapperDTO;
import com.jdt17.oauth.service.data.response.RestApiPath;
import com.jdt17.oauth.service.data.response.RestApiResponse;
import com.jdt17.oauth.service.data.response.module.OauthLoginResponse;
import com.jdt17.oauth.service.service.module.LoginInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.jdt17.oauth.service.data.utility.OauthVariable.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestApiPath.BASE_PATH + RestApiPath.OAUTH_BASE_PATH)
@Slf4j
public class LoginController {

    private final LoginInterface loginInterface;

    /**
     * Handles user login request.
     * <p>
     * This endpoint authenticates a user using their username and password.
     * It also supports additional headers for IP, timezone, user agent, and device fingerprint for logging and tracking.
     * </p>
     *
     * @param oauthLoginRequest The login request containing user credentials.
     * @param remoteIP The IP address of the client making the request.
     * @param remoteTimeZone The timezone of the client making the request.
     * @param userAgent The user agent of the client making the request.
     * @param deviceFingerprint The fingerprint of the client's device.
     * @return The OAuth login result wrapped in a RestApiResponse.
     * @throws CoreThrowHandler If there is an error during the login process.
     */
    @PostMapping(RestApiPath.OAUTH_LOGIN_PATH)
    public RestApiResponse<OauthLoginResponse> getUserLogin(
            @Valid @RequestBody OauthLoginRequest oauthLoginRequest,
            @RequestHeader(value = HEADER_NAME_USER_IP, required = false) String remoteIP,
            @RequestHeader(value = HEADER_NAME_USER_TIMEZONE, required = false) String remoteTimeZone,
            @RequestHeader(value = HEADER_NAME_FCM_TOKEN, required = false) String fcmToken,
            @RequestHeader(value = HEADER_NAME_DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = HEADER_NAME_DEVICE_REAL, required = false) String deviceReal,
            @RequestHeader(value = HEADER_NAME_DEVICE_PLATFORM, required = false) String devicePlatform,
            @RequestHeader(value = HEADER_NAME_USER_AGENT, required = false) String userAgent,
            @RequestHeader(value = HEADER_NAME_USER_FINGERPRINT, required = false) String deviceFingerprint
    ) throws CoreThrowHandler {
        RequestWrapperDTO<OauthLoginRequest> requestWrapperDTO = new RequestWrapperDTO<>(
                oauthLoginRequest,
                null,
                remoteIP,
                remoteTimeZone,
                userAgent,
                deviceFingerprint,
                fcmToken,
                deviceId,
                deviceReal,
                devicePlatform
        );

        return loginInterface.login(requestWrapperDTO);
    }
}
