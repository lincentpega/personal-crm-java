package com.lincentpega.personalcrmjava.service.telegram.matcher;

import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateMatcher;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

public class TelegramStateMatcherDecorator implements TelegramUpdateMatcher {

    private final TelegramBotState state;
    private final TelegramUpdateMatcher delegate;
    private final BotStateContainer botStateContainer;

    public TelegramStateMatcherDecorator(TelegramBotState state, TelegramUpdateMatcher delegate, BotStateContainer botStateContainer) {
        this.state = state;
        this.delegate = delegate;
        this.botStateContainer = botStateContainer;
    }

    @Override
    public boolean matches(Update update) {
        Optional<String> chatId = TelegramUtils.getChatId(update);
        if (chatId.isEmpty()) {
            return false;
        }
        TelegramBotState currentState = botStateContainer.getState(chatId.get());
        if (!currentState.equals(state)) {
            return false;
        }
        return delegate.matches(update);
    }
}
