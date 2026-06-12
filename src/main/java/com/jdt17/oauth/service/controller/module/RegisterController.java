package com.jdt17.oauth.service.controller.module;

import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.data.request.OauthRegisterRequest;
import com.jdt17.oauth.service.data.request.RequestWrapperDTO;
import com.jdt17.oauth.service.data.response.RestApiPath;
import com.jdt17.oauth.service.data.response.RestApiResponse;
import com.jdt17.oauth.service.data.response.module.OauthRegisterResponse;
import com.jdt17.oauth.service.service.module.RegisterInterface;
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
public class RegisterController {

    private final RegisterInterface registerInterface;

    @PostMapping(RestApiPath.OAUTH_REGISTER_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public RestApiResponse<OauthRegisterResponse> registerUser(
            @Valid @RequestBody OauthRegisterRequest oauthRegisterRequest,
            @RequestHeader(value = HEADER_NAME_USER_IP, required = false) String remoteIP,
            @RequestHeader(value = HEADER_NAME_USER_TIMEZONE, required = false) String remoteTimeZone,
            @RequestHeader(value = HEADER_NAME_FCM_TOKEN, required = false) String fcmToken,
            @RequestHeader(value = HEADER_NAME_DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = HEADER_NAME_DEVICE_REAL, required = false) String deviceReal,
            @RequestHeader(value = HEADER_NAME_DEVICE_PLATFORM, required = false) String devicePlatform,
            @RequestHeader(value = HEADER_NAME_USER_AGENT, required = false) String userAgent,
            @RequestHeader(value = HEADER_NAME_USER_FINGERPRINT, required = false) String deviceFingerprint
    ) throws CoreThrowHandler {

        RequestWrapperDTO<OauthRegisterRequest> requestWrapperDTO = new RequestWrapperDTO<>(
                oauthRegisterRequest,
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

        return registerInterface.register(requestWrapperDTO);
    }
}