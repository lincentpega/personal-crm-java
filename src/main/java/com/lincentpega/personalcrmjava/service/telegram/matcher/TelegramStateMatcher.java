package com.lincentpega.personalcrmjava.service.telegram.matcher;

import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateMatcher;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUtils;
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
        var chatId = TelegramUtils.getChatId(update);
        return chatId.filter(s -> stateContainer.getState(s).equals(state)).isPresent();
    }
}
