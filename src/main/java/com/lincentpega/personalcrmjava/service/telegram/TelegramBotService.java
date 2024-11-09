package com.lincentpega.personalcrmjava.service.telegram;

import com.lincentpega.personalcrmjava.configuration.TelegramProperties;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import java.util.List;

@Service
@Log4j2
public class TelegramBotService {

    private final TelegramProperties telegramProperties;
    private final TelegramBotsLongPollingApplication application;
    private final List<TelegramUpdateProcessor> updateProcessors;

    public TelegramBotService(
            TelegramProperties telegramProperties,
            TelegramBotsLongPollingApplication application,
            List<TelegramUpdateProcessor> updateProcessors) {
        this.telegramProperties = telegramProperties;
        this.application = application;
        this.updateProcessors = updateProcessors;
    }

    @SneakyThrows
    public void runBot() {
        if (telegramProperties.getBotToken().isEmpty()) {
            throw new IllegalStateException("Bot token can't be empty");
        }

        var bot = new TelegramBot();

        for (var updateProcessor : updateProcessors) {
            bot.addUpdateHandler(updateProcessor);
        }

        application.registerBot(telegramProperties.getBotToken(), bot);
    }

    @PostConstruct
    public void run() {
        runBot();
    }
}