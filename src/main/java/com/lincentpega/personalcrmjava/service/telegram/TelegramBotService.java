package com.lincentpega.personalcrmjava.service.telegram;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import com.lincentpega.personalcrmjava.configuration.TelegramProperties;

@Service
public class TelegramBotService {

    private final TelegramClient telegramClient;
    private final TelegramProperties telegramProperties;

    public TelegramBotService(TelegramClient telegramClient, TelegramProperties telegramProperties) {
        this.telegramClient = telegramClient;
        this.telegramProperties = telegramProperties;
    }

    @SneakyThrows
    public void runBot() {
        if (telegramProperties.getBotToken().isEmpty()) {
            throw new IllegalStateException("Bot token can't be empty");
        }

        var bot = new TelegramBot(telegramClient);

        try (var application = new TelegramBotsLongPollingApplication()) {
            application.registerBot(telegramProperties.getBotToken(), bot);
            application.start();
        }
    }
}