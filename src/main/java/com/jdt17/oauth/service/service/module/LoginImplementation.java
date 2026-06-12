package com.jdt17.oauth.service.service.module;

import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.data.module.UserDTO;
import com.jdt17.oauth.service.data.request.OauthLoginRequest;
import com.jdt17.oauth.service.data.request.RequestWrapperDTO;
import com.jdt17.oauth.service.data.response.RestApiError;
import com.jdt17.oauth.service.data.response.RestApiResponse;
import com.jdt17.oauth.service.data.response.module.OauthLoginResponse;
import com.jdt17.oauth.service.service.repository.OauthRepository;
import com.jdt17.oauth.service.service.utility.OauthJWTUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginImplementation implements LoginInterface {
    private static final int ATT_ACCESS_TTL_SECONDS = 90;
    /* ini but integrate redis */
    private final RedisTemplate<String, Object> redisTemplate;

    private final OauthJWTUtility oauthJWTUtility;

    private final OauthRepository oauthRepository;


    private final MessageSource messageSource;


    @Override
    public RestApiResponse<OauthLoginResponse> login(RequestWrapperDTO<OauthLoginRequest> requestWrapperDTO) throws CoreThrowHandler {

        /* LOGIN BUAT LOGIC */

        OauthLoginResponse oauthLoginResponse = new OauthLoginResponse();

        OauthLoginRequest request = requestWrapperDTO.getRequest();

        Optional<UserDTO> userDTO = oauthRepository.getByUsersEmail(request.getOauthLoginRequestEmail());

        if(userDTO.isEmpty()) {
            throw new CoreThrowHandler(RestApiError.ACCOUNT_NOT_FOUND, messageSource);
        }

        String jwt = oauthJWTUtility.generateOAUTHToken(userDTO.get(), ATT_ACCESS_TTL_SECONDS, requestWrapperDTO.getRequestRemoteIp());

        oauthLoginResponse.setAuthToken(jwt);
        oauthLoginResponse.setUsersFullName(userDTO.get().getUsersFullname());

        return RestApiResponse.<OauthLoginResponse>builder()
                .restApiResponseHttpCode(HttpStatus.OK.value())
                .restApiResponseResult(oauthLoginResponse)
                .restApiResponseMessage("KAMU BERHASIL LOGIN")
                .build();
    }
}
