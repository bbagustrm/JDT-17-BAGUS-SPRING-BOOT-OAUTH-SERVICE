package com.jdt17.oauth.service.components.handler;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceProvider {
    @Getter
    private static MessageSource messageSource;

    @Autowired
    public MessageSourceProvider(MessageSource messageSource) {
        MessageSourceProvider.messageSource = messageSource;
    }

}
