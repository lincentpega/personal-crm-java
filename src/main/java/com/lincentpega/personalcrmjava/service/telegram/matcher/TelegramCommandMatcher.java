package com.lincentpega.personalcrmjava.service.telegram.matcher;

import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramCommandMatcher extends TelegramStateMatcher {

    private final String command;

    public TelegramCommandMatcher(String command, TelegramBotState state, BotStateContainer stateContainer) {
        super(state, stateContainer);
        this.command = command;
    }

    @Override
    public boolean matches(Update update) {
        var superMatch = super.matches(update);
        if (!superMatch) {
            return false;
        }
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
