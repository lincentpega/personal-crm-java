package com.lincentpega.personalcrmjava.service.telegram.matcher.contact;

import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import com.lincentpega.personalcrmjava.service.telegram.TelegramContactCallbackValue;
import com.lincentpega.personalcrmjava.service.telegram.matcher.TelegramStateMatcher;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramSaveContactCallbackMatcher extends TelegramStateMatcher {
    
    public TelegramSaveContactCallbackMatcher(BotStateContainer stateContainer) {
        super(TelegramBotState.CREATE_CONTACT, stateContainer);
    }

    @Override
    public boolean matches(Update update) {
        if (!super.matches(update)) {
            return false;
        }
        if (!update.hasCallbackQuery()) {
            return false;
        }
        return update.getCallbackQuery().getData().equals(TelegramContactCallbackValue.SAVE_CONTACT.getCallbackData());
    }
}
