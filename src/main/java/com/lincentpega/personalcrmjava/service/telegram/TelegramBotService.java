package com.lincentpega.personalcrmjava.service.telegram;

import com.lincentpega.personalcrmjava.configuration.TelegramProperties;
import com.lincentpega.personalcrmjava.service.telegram.handler.TelegramChatIdHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.TelegramStartHandler;
import com.lincentpega.personalcrmjava.service.telegram.matcher.TelegramCommandMatcher;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Service
@Log4j2
public class TelegramBotService {

    private final TelegramProperties telegramProperties;
    private final TelegramChatIdHandler chatIdHandler;
    private final TelegramStartHandler startHandler;
    private final TelegramBotsLongPollingApplication application;

    public TelegramBotService(
            TelegramProperties telegramProperties,
            TelegramChatIdHandler chatIdHandler,
            TelegramStartHandler startHandler,
            TelegramBotsLongPollingApplication application) {
        this.telegramProperties = telegramProperties;
        this.chatIdHandler = chatIdHandler;
        this.startHandler = startHandler;
        this.application = application;
    }

    @SneakyThrows
    public void runBot() {
        if (telegramProperties.getBotToken().isEmpty()) {
            throw new IllegalStateException("Bot token can't be empty");
        }

        var bot = new TelegramBot();

        bot.addUpdateHandler(new TelegramUpdateProcessor(new TelegramCommandMatcher("/chat_id"), chatIdHandler));
        bot.addUpdateHandler(new TelegramUpdateProcessor(new TelegramCommandMatcher("/start"), startHandler));

        application.registerBot(telegramProperties.getBotToken(), bot);
    }

    @PostConstruct
    public void run() {
        runBot();
    }
}