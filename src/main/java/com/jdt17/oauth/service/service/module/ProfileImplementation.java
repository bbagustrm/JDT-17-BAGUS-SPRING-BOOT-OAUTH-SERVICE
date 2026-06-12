package com.jdt17.oauth.service.service.module;

import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.data.module.UserDTO;
import com.jdt17.oauth.service.data.request.RequestWrapperDTO;
import com.jdt17.oauth.service.data.response.RestApiResponse;
import com.jdt17.oauth.service.data.response.module.OauthLoginResponse;
import com.jdt17.oauth.service.service.repository.OauthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImplementation implements ProfileInterface {

    private final OauthRepository oauthRepository;

    @Override
    public RestApiResponse<OauthLoginResponse> getProfile(RequestWrapperDTO<Object> requestWrapperDTO) throws CoreThrowHandler {
        log.info("USERS ID YG DILEMPAR GATEWAY --- {}", requestWrapperDTO.getRequestUsersId());

        Optional<UserDTO> userDTOOptional = oauthRepository.getByUsersId(UUID.fromString(requestWrapperDTO.getRequestUsersId()));

        OauthLoginResponse oauthLoginResponse = new OauthLoginResponse();
        oauthLoginResponse.setUsersFullName(userDTOOptional.get().getUsersFullname());

        /* PROFILE BUAT LOGIC */
        return RestApiResponse.<OauthLoginResponse>builder()
                .restApiResponseHttpCode(HttpStatus.OK.value())
                .restApiResponseMessage("KAMU DI PROFILE")
                .restApiResponseResult(oauthLoginResponse)
                .build();
    }
}
