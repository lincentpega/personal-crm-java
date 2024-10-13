package com.lincentpega.personalcrmjava.service.telegram.matcher;

import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateMatcher;
import org.springframework.lang.Nullable;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramStateMatcher implements TelegramUpdateMatcher {

    private final TelegramBotState state;
    private final BotStateContainer stateContainer;

    public TelegramStateMatcher(TelegramBotState state, BotStateContainer stateContainer) {
        this.state = state;
        this.stateContainer = stateContainer;
    }

    @Override
    public boolean matches(Update update) {
        var chatId = getChatId(update);
        if (chatId == null) {
            return false;
        }
        return stateContainer.getState(chatId).equals(state);
    }

    @Nullable
    private String getChatId(Update update) {
        Long chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            return null;
        }
        return chatId.toString();
    }
}
