package com.jdt17.oauth.service.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestApiResponse<T> {
    @JsonProperty("code")
    private int restApiResponseHttpCode;

    @JsonProperty("result")
    private T restApiResponseResult;

    @JsonProperty("message")
    private String restApiResponseMessage;

    @JsonProperty("error")
    private Map<String, Serializable> restApiResponseError;


}
