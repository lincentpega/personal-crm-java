package com.lincentpega.personalcrmjava.service.telegram.handler.contact;

import com.lincentpega.personalcrmjava.service.telegram.*;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramCreateContactHandler implements TelegramUpdateHandlerFunc {

    private final TelegramClient telegramClient;
    private final ContactMessageBuilder contactMessageBuilder;
    private final BotStateContainer botStateContainer;

    public TelegramCreateContactHandler(TelegramClient telegramClient, ContactMessageBuilder contactMessageBuilder, BotStateContainer botStateContainer) {
        this.telegramClient = telegramClient;
        this.contactMessageBuilder = contactMessageBuilder;
        this.botStateContainer = botStateContainer;
    }

    @SneakyThrows
    @Override
    public void handle(Update update) {
        var chatId = TelegramUtils.getChatIdNonNull(update);
        var keyboardMarkup = TelegramKeyboards.createContactKeyboard();
        var response = new SendMessage(chatId, contactMessageBuilder.buildMessage(chatId));
        response.setReplyMarkup(keyboardMarkup);
        botStateContainer.setState(chatId, TelegramBotState.CREATE_CONTACT);
        telegramClient.execute(response);
    }
}
