package com.jdt17.oauth.service.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthLoginRequest {
    @JsonProperty("email")
    @NotBlank(message = "Fill in your e-mail or cellphone number")
    private String oauthLoginRequestEmail;

    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Minimum 4 characters in password")
    private String oauthLoginRequestPassword;
}
