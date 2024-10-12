package com.lincentpega.personalcrmjava.service.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramUpdateProcessor {

    private final TelegramUpdateMatcher matcher;
    private final TelegramUpdateHandlerFunc handlerFunc;

    public TelegramUpdateProcessor(TelegramUpdateMatcher matcher, TelegramUpdateHandlerFunc handlerFunc) {
        this.matcher = matcher;
        this.handlerFunc = handlerFunc;
    }

    public boolean process(Update update) {
        if (matcher.matches(update)) {
            handlerFunc.handle(update);
            return true;
        }

        return false;
    }
}
