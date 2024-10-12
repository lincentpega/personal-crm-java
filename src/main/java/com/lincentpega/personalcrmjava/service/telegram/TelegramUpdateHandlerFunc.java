package com.lincentpega.personalcrmjava.service.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

@FunctionalInterface
public interface TelegramUpdateHandlerFunc {

    void handle(Update update);
}
