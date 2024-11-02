package com.lincentpega.personalcrmjava.service.telegram.matcher.contact;

import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateMatcher;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class TelegramContactSetFieldMatcher implements TelegramUpdateMatcher {

    public static final List<TelegramBotState> VALID_STATES = List.of(
            TelegramBotState.CONTACT_SET_FIRST_NAME,
            TelegramBotState.CONTACT_SET_MIDDLE_NAME,
            TelegramBotState.CONTACT_SET_LAST_NAME,
            TelegramBotState.CONTACT_SET_BIRTHDATE,
            TelegramBotState.CONTACT_SET_GENDER
    );
    private final BotStateContainer botStateContainer;

    public TelegramContactSetFieldMatcher(BotStateContainer botStateContainer) {
        this.botStateContainer = botStateContainer;
    }

    @Override
    public boolean matches(Update update) {
        var chatId = TelegramUtils.getChatIdNonNull(update);
        if (!update.hasMessage()) {
            return false;
        }
        return VALID_STATES.contains(botStateContainer.getState(chatId));
    }
}
