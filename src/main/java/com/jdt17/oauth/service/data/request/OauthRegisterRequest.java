package com.jdt17.oauth.service.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthRegisterRequest {

    @JsonProperty("full_name")
    @NotBlank(message = "Full name is required")
    private String oauthRegisterRequestFullName;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is not valid")
    private String oauthRegisterRequestEmail;

    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Minimum 4 characters in password")
    private String oauthRegisterRequestPassword;
}