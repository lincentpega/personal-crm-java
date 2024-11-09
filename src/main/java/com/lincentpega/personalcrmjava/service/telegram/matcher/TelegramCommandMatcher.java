package com.lincentpega.personalcrmjava.service.telegram.matcher;

import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateMatcher;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramCommandMatcher implements TelegramUpdateMatcher {

    private final String command;

    public TelegramCommandMatcher(String command) {
        this.command = command;
    }

    @Override
    public boolean matches(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();
            if (message.hasText()) {
                var text = message.getText();
                return text.trim().startsWith(command);
            }
            return false;
        }
        return false;
    }
}
