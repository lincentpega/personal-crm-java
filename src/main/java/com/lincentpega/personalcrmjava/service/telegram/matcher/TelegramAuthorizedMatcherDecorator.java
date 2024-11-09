package com.lincentpega.personalcrmjava.service.telegram.matcher;

import com.lincentpega.personalcrmjava.service.telegram.TelegramAuthService;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateMatcher;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class TelegramAuthorizedMatcherDecorator implements TelegramUpdateMatcher {

    private final TelegramUpdateMatcher delegate;
    private final TelegramAuthService authService;

    public TelegramAuthorizedMatcherDecorator(TelegramUpdateMatcher delegate, TelegramAuthService authService) {
        this.delegate = delegate;
        this.authService = authService;
    }

    @Override
    public boolean matches(Update update) {
        Optional<String> chatId = TelegramUtils.getChatId(update);
        if (chatId.isEmpty()) {
            return false;
        }

        if (!authService.isAuthorized(chatId.get())) {
            return false;
        }

        return delegate.matches(update);
    }
}
