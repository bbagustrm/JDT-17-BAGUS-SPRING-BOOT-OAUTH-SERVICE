package com.jdt17.oauth.service.controller.handler;

import com.jdt17.oauth.service.components.runtime.exception.CoreThrowHandler;
import com.jdt17.oauth.service.data.response.RestApiError;
import com.jdt17.oauth.service.data.response.RestApiResponse;
import com.jdt17.oauth.service.data.response.ValidationErrorApiResponse;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RestControllerAdviceHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(buildLegacyValidationBody(ex.getBindingResult().getFieldErrors()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ValidationErrorApiResponse> handleWebExchangeBindException(WebExchangeBindException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(buildLegacyValidationBody(ex.getFieldErrors()));
    }

    private ValidationErrorApiResponse buildLegacyValidationBody(Iterable<FieldError> fieldErrors) {
        Map<String, String> field = new LinkedHashMap<>();
        for (FieldError fieldError : fieldErrors) {
            String apiKey = normalizeValidationField(fieldError);
            String text = resolveFieldErrorMessage(fieldError);
            field.merge(apiKey, text, (a, b) -> b);
        }
        return ValidationErrorApiResponse.builder()
                .field(field)
                .message(null)
                .code("BAD_REQUEST")
                .build();
    }

    /**
     * OAuth {@code /oauth/login} uses JSON {@code account} — errors expose {@code account}.
     * Admin login uses {@code account}/{@code email} — errors expose {@code email}.
     */
    private String normalizeValidationField(FieldError fieldError) {
        String raw = fieldError.getField();
        String objectName = fieldError.getObjectName();
        if (raw == null || raw.isBlank()) {
            return raw;
        }
        boolean oauthLoginBody = "oauthLoginRequest".equals(objectName)
                || ("request".equals(objectName) && isRequestUriSuffix("/oauth/login"));
        boolean adminLoginBody = "adminLoginRequest".equals(objectName)
                || ("request".equals(objectName) && isRequestUriSuffix("/admin/login"));

        if ("oauthLoginRequestPassword".equals(raw)) {
            return "password";
        }
        if ("oauthLoginRequestEmail".equals(raw) || "account".equals(raw)) {
            if (oauthLoginBody) {
                return "account";
            }
            if (adminLoginBody) {
                return "email";
            }
            return "email";
        }
        return raw;
    }

    private static boolean isRequestUriSuffix(String suffix) {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (!(attrs instanceof ServletRequestAttributes servletAttrs)) {
            return false;
        }
        String uri = servletAttrs.getRequest().getRequestURI();
        return uri != null && uri.endsWith(suffix);
    }

    private String resolveFieldErrorMessage(FieldError fieldError) {
        try {
            return messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "";
        }
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<RestApiResponse<Void>> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        Map<String, Serializable> errors = new HashMap<>();

        // put the missing field name and a custom message
        errors.put(ex.getRequestPartName(), "Field is required but not present in the request");

        RestApiResponse<Void> apiResponse = new RestApiResponse<>();
        apiResponse.setRestApiResponseHttpCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setRestApiResponseError(errors);
        apiResponse.setRestApiResponseMessage(BAD_REQUEST.getReasonPhrase());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<RestApiResponse<Void>> handleServerWebInputException(ServerWebInputException ex) {
        RestApiResponse<Void> apiResponse = new RestApiResponse<>();
        apiResponse.setRestApiResponseHttpCode(BAD_REQUEST.value());
        apiResponse.setRestApiResponseMessage(ex.getMessage());

        return ResponseEntity.status(BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<RestApiResponse<Void>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        RestApiResponse<Void> apiResponse = new RestApiResponse<>();
        apiResponse.setRestApiResponseHttpCode(ex.getStatusCode().value());
        apiResponse.setRestApiResponseMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<RestApiResponse<Void>> handleResponseStatusException(ResponseStatusException ex) {
        RestApiResponse<Void> apiResponse = new RestApiResponse<>();
        apiResponse.setRestApiResponseHttpCode(ex.getStatusCode().value());
        apiResponse.setRestApiResponseMessage(ex.getMessage());

        return ResponseEntity.status(ex.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<RestApiResponse<Void>> handleServletException(ServletException ex) {
        RestApiResponse<Void> apiResponse = new RestApiResponse<>();
        apiResponse.setRestApiResponseHttpCode(INTERNAL_SERVER_ERROR.value());
        apiResponse.setRestApiResponseMessage(ex.getMessage());

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RestApiResponse<Void>> handleNoResourceFoundException(ServletException ex) {
        RestApiResponse<Void> apiResponse = new RestApiResponse<>();
        apiResponse.setRestApiResponseHttpCode(NOT_FOUND.value());
        apiResponse.setRestApiResponseMessage(ex.getMessage());

        return ResponseEntity.status(NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(CoreThrowHandler.class)
    public ResponseEntity<RestApiResponse<Void>> handleCoreThrowHandler(CoreThrowHandler ex) {
        RestApiResponse<Void> apiResponse = new RestApiResponse<>();
        apiResponse.setRestApiResponseHttpCode(ex.getCode());
        apiResponse.setRestApiResponseError(ex.getError());
        apiResponse.setRestApiResponseMessage(ex.getMessage());

        return ResponseEntity.status(ex.getCode()).body(apiResponse);
    }


    /** (Optional) narrower catch for RuntimeException if you want */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RestApiResponse<Void>> handleRuntime(RuntimeException ex) {
        return handleAnyThrowable(ex);
    }


    /** Fallback: anything not matched above -> 500 */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<RestApiResponse<Void>> handleAnyThrowable(Throwable ex) {
        // generate an errorId so you can correlate logs <-> response
        String errorId = java.util.UUID.randomUUID().toString();
        log.error("[{}] Unhandled exception", errorId, ex);

        RestApiResponse<Void> api = new RestApiResponse<>();
        api.setRestApiResponseHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        String message = new CoreThrowHandler(RestApiError.INTERNAL_SERVER_ERROR, null, messageSource).getMessage();

        api.setRestApiResponseMessage(message);
        // optional: include safe diagnostics for a client
        Map<String, Serializable> err = new HashMap<>();
        err.put("errorId", errorId);
        err.put("reason", "Unexpected error");
        api.setRestApiResponseError(err);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(api);
    }

}
