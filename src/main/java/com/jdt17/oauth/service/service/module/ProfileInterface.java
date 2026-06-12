package com.jdt17.oauth.service.service.module;

import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.data.request.OauthLoginRequest;
import com.jdt17.oauth.service.data.request.RequestWrapperDTO;
import com.jdt17.oauth.service.data.response.RestApiResponse;
import com.jdt17.oauth.service.data.response.module.OauthLoginResponse;

public interface ProfileInterface {
    RestApiResponse<OauthLoginResponse> getProfile(RequestWrapperDTO<Object> requestWrapperDTO) throws CoreThrowHandler;
}
