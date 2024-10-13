package com.lincentpega.personalcrmjava.service.telegram.matcher;

import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramContactCallbackMatcher extends TelegramStateMatcher {

    public TelegramContactCallbackMatcher(BotStateContainer stateContainer) {
        super(TelegramBotState.CREATE_CONTACT, stateContainer);
    }

    @Override
    public boolean matches(Update update) {
        var superMatch = super.matches(update);
        if (!superMatch) {
            return false;
        }
        if (!update.hasCallbackQuery()) {
            return false;
        }
        var query = update.getCallbackQuery();
        var callbackData = query.getData();
        return callbackData.startsWith("#create_contact");
    }
}
