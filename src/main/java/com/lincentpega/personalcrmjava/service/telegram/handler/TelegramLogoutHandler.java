package com.lincentpega.personalcrmjava.service.telegram.handler;

import com.lincentpega.personalcrmjava.data.TelegramSessionRepository;
import com.lincentpega.personalcrmjava.domain.TelegramSession;
import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateHandlerFunc;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

public class TelegramLogoutHandler implements TelegramUpdateHandlerFunc {

    private final TelegramSessionRepository telegramSessionRepository;
    private final BotStateContainer botStateContainer;
    private final TelegramClient telegramClient;

    public TelegramLogoutHandler(TelegramSessionRepository telegramSessionRepository, BotStateContainer botStateContainer, TelegramClient telegramClient) {
        this.telegramSessionRepository = telegramSessionRepository;
        this.botStateContainer = botStateContainer;
        this.telegramClient = telegramClient;
    }

    @SneakyThrows
    @Override
    public void handle(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        Optional<TelegramSession> telegramSessionOpt = telegramSessionRepository.findByChatId(chatId);
        if (telegramSessionOpt.isEmpty()) {
            telegramClient.execute(new SendMessage(chatId, "Выход из аккаунты уже выполнен"));
            return;
        }

        TelegramSession telegramSession = telegramSessionOpt.get();
        telegramSession.setChatId(null);
        telegramSessionRepository.save(telegramSession);
        botStateContainer.clearValues(chatId);
        botStateContainer.setState(chatId, TelegramBotState.INITIAL);
    }
}
