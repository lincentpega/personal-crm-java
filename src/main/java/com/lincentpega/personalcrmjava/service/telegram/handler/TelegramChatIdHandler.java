package com.lincentpega.personalcrmjava.service.telegram.handler;

import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateHandlerFunc;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Log4j2
@Service
public class TelegramChatIdHandler implements TelegramUpdateHandlerFunc {

    private final TelegramClient telegramClient;

    public TelegramChatIdHandler(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public void handle(Update update) {
        try {
            var chatId = update.getMessage().getChatId().toString();
            var response = new SendMessage(chatId, "Ваш chat id: " + chatId);

            telegramClient.execute(response);
        } catch (TelegramApiException e) {
            log.error("Failed to send update to telegram", e);
        }
    }
}
