package com.lincentpega.personalcrmjava.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfiguration {

    @Bean
    TelegramClient telegramClient(TelegramProperties telegramProperties) {
        if (telegramProperties.getBotToken().isEmpty()) {
            throw new IllegalStateException("Bot token is not defined");
        }

        return new OkHttpTelegramClient(telegramProperties.getBotToken());
    }

    @Bean
    TelegramBotsLongPollingApplication telegramBotsLongPollingApplication() {
        return new TelegramBotsLongPollingApplication();
    }
}
