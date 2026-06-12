package com.jdt17.oauth.service.service.module;

import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.data.request.OauthRegisterRequest;
import com.jdt17.oauth.service.data.request.RequestWrapperDTO;
import com.jdt17.oauth.service.data.response.RestApiResponse;
import com.jdt17.oauth.service.data.response.module.OauthRegisterResponse;

public interface RegisterInterface {

    RestApiResponse<OauthRegisterResponse> register(
            RequestWrapperDTO<OauthRegisterRequest> requestWrapperDTO
    ) throws CoreThrowHandler;
}