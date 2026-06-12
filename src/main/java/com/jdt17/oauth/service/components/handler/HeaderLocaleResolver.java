package com.jdt17.oauth.service.components.handler;

import com.jdt17.oauth.service.data.utility.OauthVariable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Slf4j
public class HeaderLocaleResolver implements LocaleResolver {
    private final Locale defaultLocale = Locale.ENGLISH;

    @Override
    @NonNull
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(OauthVariable.HEADER_ACCEPTED_LANGUAGE); // ACCEPT-LANGUAGE

        if (lang == null || lang.isBlank()) {
            return defaultLocale;
        }
        return Locale.forLanguageTag(lang);
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest,
                          HttpServletResponse response,
                          Locale locale) {
        // not needed for header-based
    }
}
