package com.lincentpega.personalcrmjava.service.telegram;

import com.lincentpega.personalcrmjava.configuration.TelegramProperties;
import com.lincentpega.personalcrmjava.service.telegram.handler.TelegramChatIdHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.TelegramCreateContactHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.TelegramStartHandler;
import com.lincentpega.personalcrmjava.service.telegram.matcher.TelegramCommandMatcher;
import com.lincentpega.personalcrmjava.service.telegram.matcher.TelegramContactCallbackMatcher;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Service
@Log4j2
public class TelegramBotService {

    private final TelegramProperties telegramProperties;
    private final TelegramBotsLongPollingApplication application;
    private final ApplicationContext applicationContext;
    private final BotStateContainer botStateContainer;

    public TelegramBotService(
            TelegramProperties telegramProperties,
            TelegramBotsLongPollingApplication application,
            ApplicationContext applicationContext,
            BotStateContainer botStateContainer) {
        this.telegramProperties = telegramProperties;
        this.application = application;
        this.applicationContext = applicationContext;
        this.botStateContainer = botStateContainer;
    }

    @SneakyThrows
    public void runBot() {
        if (telegramProperties.getBotToken().isEmpty()) {
            throw new IllegalStateException("Bot token can't be empty");
        }

        var bot = new TelegramBot();

        bot.addUpdateHandler(
                new TelegramUpdateProcessor(
                        new TelegramCommandMatcher("/chat_id", TelegramBotState.INITIAL, botStateContainer),
                        new TelegramChatIdHandler(applicationContext)
                )
        );
        bot.addUpdateHandler(
                new TelegramUpdateProcessor(
                        new TelegramCommandMatcher("/start", TelegramBotState.INITIAL, botStateContainer),
                        new TelegramStartHandler(applicationContext)
                )
        );
        bot.addUpdateHandler(
                new TelegramUpdateProcessor(
                        new TelegramCommandMatcher("/create_contact", TelegramBotState.INITIAL, botStateContainer),
                        new TelegramCreateContactHandler(applicationContext)
                )
        );
        bot.addUpdateHandler(
                new TelegramUpdateProcessor(
                        new TelegramContactCallbackMatcher(botStateContainer),
                        new TelegramCreateContactHandler(applicationContext)
                )
        );

        application.registerBot(telegramProperties.getBotToken(), bot);
    }

    @PostConstruct
    public void run() {
        runBot();
    }
}