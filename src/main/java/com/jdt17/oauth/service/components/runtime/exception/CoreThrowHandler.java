package com.jdt17.oauth.service.components.runtime.exception;

import com.jdt17.oauth.service.data.response.RestApiError;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
public class CoreThrowHandler extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String message;
    private final Integer code;
    private final Map<String, Serializable> error;

    public CoreThrowHandler(Integer status,
                            String message,
                            Map<String, Serializable> error
    ) {
        super(message);
        this.code = status;
        this.message = message;
        this.error = error;
    }

    public static Mono<CoreThrowHandler> create(
            ServerWebExchange serverWebExchange,
            RestApiError restApiError,
            MessageSource messageSource
    ) {
        Map<String, Serializable> error = new HashMap<>();

        return restApiError.getRestApiError(messageSource, serverWebExchange)
                .map(restApiErrorDTO -> new CoreThrowHandler(
                        restApiErrorDTO.getCode(),
                        restApiErrorDTO.getMessage(),
                        error
                ));
    }


    public CoreThrowHandler(RestApiError restApiError,
                            Map<String, Serializable> error,
                            MessageSource messageSource
    ) {
        super();
        this.code = restApiError.getCode();
        this.error = error;

        Locale locale = LocaleContextHolder.getLocale();

        this.message = messageSource.getMessage(
                restApiError.getMessage(),
                null,
                restApiError.getMessage(),
                locale
        );
    }

    public CoreThrowHandler(
            RestApiError restApiError,
            Map<String, Serializable> error,
            String extraErrorMessage,
            MessageSource messageSource
    ) {
        super();

        this.code = restApiError.getCode();
        this.error = error;

        Locale locale = LocaleContextHolder.getLocale();


        this.message = messageSource.getMessage(
                restApiError.getMessage(),
                null,
                restApiError.getMessage(),
                locale
        ) + " " + extraErrorMessage;
    }

    public CoreThrowHandler(RestApiError restApiError, MessageSource messageSource) {
        super();
        this.code = restApiError.getCode();
        this.error = Collections.emptyMap();
        Locale locale = LocaleContextHolder.getLocale();
        // Fetch the translated message using the message key from the enum
        this.message = messageSource.getMessage(restApiError.getMessage(), null, restApiError.getMessage(), locale);
    }

}
