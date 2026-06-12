package com.jdt17.oauth.service.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Locale;

@AllArgsConstructor
@Getter
@Slf4j
public enum RestApiError {

    ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "error.http.account_not_found"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "error.http.account_not_found"),
    X_API_ACCESS_KEY(HttpStatus.UNAUTHORIZED.value(), "error.http.api_key_not_found"),
    X_AUTH_ACCESS_KEY(HttpStatus.UNAUTHORIZED.value(), "error.http.authorization_not_valid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error.http.internal_server_error");

    private final int code;
    private final String message;


    public Mono<RestApiErrorDTO> getRestApiError(
            MessageSource messageSource, ServerWebExchange serverWebExchange) {

        return Mono.deferContextual(
                context -> {

                    String language = serverWebExchange.
                            getRequest().
                            getHeaders().
                            getFirst("Accept-Language");

                    log.info("ACCEPT-LANGUAGE: {}", language);

                    Locale locale = language != null ? Locale.forLanguageTag(language) : Locale.ENGLISH;

                    /* GET DATA TRANSLATION */

                    String  translatedMessage = null;

                    try {
                        translatedMessage = messageSource
                                .getMessage(this.message, null, locale);
                    } catch (Exception exception) {

                        log.error("MESSAGE KEY -- {} not found in message properties for locale {}",
                                this.message,
                                locale
                        );
                        translatedMessage = "DEFAULT ERROR MESSAGE";
                    }




                    log.info("TRANSLATED message for key: {}: {}",
                            this.message, translatedMessage
                    );
                    return Mono.just(new RestApiErrorDTO(
                            this.code,
                            this.message
                    ));
                }
        );
    }



}
