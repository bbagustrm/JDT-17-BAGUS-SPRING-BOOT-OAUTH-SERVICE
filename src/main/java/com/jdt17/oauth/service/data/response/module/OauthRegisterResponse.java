package com.jdt17.oauth.service.data.response.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthRegisterResponse {

    @JsonProperty("users_full_name")
    private String usersFullName;
}