package com.lincentpega.personalcrmjava.service.telegram.handler;

import com.lincentpega.personalcrmjava.service.telegram.ContactMessageBuilder;
import com.lincentpega.personalcrmjava.service.telegram.TelegramKeyboards;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateHandlerFunc;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUtils;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramContactCallbackHandler implements TelegramUpdateHandlerFunc {

    private final TelegramClient telegramClient;
    private final ContactMessageBuilder contactMessageBuilder;

    public TelegramContactCallbackHandler(ApplicationContext applicationContext) {
        this.contactMessageBuilder = applicationContext.getBean(ContactMessageBuilder.class);
        this.telegramClient = applicationContext.getBean(TelegramClient.class);
    }

    @Override
    public void handle(Update update) {
        var chatId = TelegramUtils.getChatIdNonNull(update);
        var personMessage = contactMessageBuilder.buildMessage(chatId);
        var keyboard = TelegramKeyboards.createContactKeyboard();
    }
}
