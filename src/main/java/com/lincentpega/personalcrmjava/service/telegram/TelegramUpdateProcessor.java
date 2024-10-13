package com.lincentpega.personalcrmjava.service.telegram;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
public class TelegramUpdateProcessor {

    private final TelegramUpdateMatcher matcher;
    private final TelegramUpdateHandlerFunc handlerFunc;

    public TelegramUpdateProcessor(TelegramUpdateMatcher matcher, TelegramUpdateHandlerFunc handlerFunc) {
        this.matcher = matcher;
        this.handlerFunc = handlerFunc;
    }

    public boolean process(Update update) {
        try {
            if (matcher.matches(update)) {
                handlerFunc.handle(update);
                return true;
            }

            return false;
        } catch (Exception e) {
            log.error("Failed to process update", e);
            return false;
        }
    }
}
