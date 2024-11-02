package com.lincentpega.personalcrmjava.service.telegram.handler.contact;

import com.lincentpega.personalcrmjava.service.telegram.*;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramContactCallbackHandler implements TelegramUpdateHandlerFunc {

    private final TelegramClient telegramClient;
    private final BotStateContainer botStateContainer;

    public TelegramContactCallbackHandler(ApplicationContext applicationContext) {
        this.telegramClient = applicationContext.getBean(TelegramClient.class);
        this.botStateContainer = applicationContext.getBean(BotStateContainer.class);
    }

    @SneakyThrows
    @Override
    public void handle(Update update) {
        var chatId = TelegramUtils.getChatIdNonNull(update);
        var callbackData = update.getCallbackQuery().getData();

        String fieldName;
        if (TelegramContactCallbackValue.SET_FIRST_NAME.getCallbackData().equals(callbackData)) {
            botStateContainer.setState(chatId, TelegramBotState.CONTACT_SET_FIRST_NAME);
            fieldName = "first name";
        } else if (TelegramContactCallbackValue.SET_MIDDLE_NAME.getCallbackData().equals(callbackData)) {
            botStateContainer.setState(chatId, TelegramBotState.CONTACT_SET_MIDDLE_NAME);
            fieldName = "middle name";
        } else if (TelegramContactCallbackValue.SET_LAST_NAME.getCallbackData().equals(callbackData)) {
            botStateContainer.setState(chatId, TelegramBotState.CONTACT_SET_LAST_NAME);
            fieldName = "last name";
        } else if (TelegramContactCallbackValue.SET_BIRHTDATE.getCallbackData().equals(callbackData)) {
            botStateContainer.setState(chatId, TelegramBotState.CONTACT_SET_BIRTHDATE);
            fieldName = "birthdate";
        } else if (TelegramContactCallbackValue.SET_GENDER.getCallbackData().equals(callbackData)) {
            botStateContainer.setState(chatId, TelegramBotState.CONTACT_SET_GENDER);
            fieldName = "gender";
        } else {
            telegramClient.execute(new SendMessage(chatId, "Invalid state"));
            return;
        }

        String message = "Enter value for " + fieldName;
        var response = new EditMessageText(message);
        response.setReplyMarkup(null);
        response.setChatId(chatId);
        response.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        telegramClient.execute(response);
    }
}
