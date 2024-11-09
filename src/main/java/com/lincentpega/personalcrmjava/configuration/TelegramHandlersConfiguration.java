package com.lincentpega.personalcrmjava.configuration;

import com.lincentpega.personalcrmjava.data.TelegramSessionRepository;
import com.lincentpega.personalcrmjava.service.person.PersonService;
import com.lincentpega.personalcrmjava.service.telegram.*;
import com.lincentpega.personalcrmjava.service.telegram.handler.TelegramChatIdHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.TelegramLogoutHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.TelegramStartHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.contact.TelegramContactCallbackHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.contact.TelegramContactSetFieldHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.contact.TelegramCreateContactHandler;
import com.lincentpega.personalcrmjava.service.telegram.handler.contact.TelegramSaveContactHandler;
import com.lincentpega.personalcrmjava.service.telegram.matcher.TelegramAuthorizedMatcherDecorator;
import com.lincentpega.personalcrmjava.service.telegram.matcher.TelegramCommandMatcher;
import com.lincentpega.personalcrmjava.service.telegram.matcher.TelegramStateMatcherDecorator;
import com.lincentpega.personalcrmjava.service.telegram.matcher.contact.TelegramContactCallbackMatcher;
import com.lincentpega.personalcrmjava.service.telegram.matcher.contact.TelegramContactSetFieldMatcher;
import com.lincentpega.personalcrmjava.service.telegram.matcher.contact.TelegramSaveContactCallbackMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramHandlersConfiguration {

    @Bean
    TelegramUpdateProcessor chatIdUpdateProcessor(TelegramClient telegramClient) {
        return new TelegramUpdateProcessor(
                new TelegramCommandMatcher("/chat_id"),
                new TelegramChatIdHandler(telegramClient)
        );
    }

    @Bean
    TelegramUpdateProcessor startUpdateProcessor(BotStateContainer botStateContainer, TelegramClient telegramClient,
                                                 TelegramSessionRepository telegramSessionRepository) {
        var matcher = new TelegramStateMatcherDecorator(
                TelegramBotState.INITIAL,
                new TelegramCommandMatcher("/start"),
                botStateContainer
        );
        var handler = new TelegramStartHandler(telegramClient, telegramSessionRepository);
        return new TelegramUpdateProcessor(
                matcher,
                handler
        );
    }

    @Bean
    TelegramUpdateProcessor createContactProcessor(BotStateContainer botStateContainer, TelegramClient telegramClient,
                                                   ContactMessageBuilder contactMessageBuilder, TelegramAuthService authService) {
        var matcher = new TelegramAuthorizedMatcherDecorator(
                new TelegramStateMatcherDecorator(
                        TelegramBotState.INITIAL,
                        new TelegramCommandMatcher("/create_contact"),
                        botStateContainer
                ),
                authService
        );
        var handler = new TelegramCreateContactHandler(telegramClient, contactMessageBuilder, botStateContainer);
        return new TelegramUpdateProcessor(
                matcher,
                handler
        );
    }

    @Bean
    TelegramUpdateProcessor contactCallbackProcessor(BotStateContainer botStateContainer, TelegramClient telegramClient,
                                                     TelegramAuthService telegramAuthService) {
        var matcher = new TelegramAuthorizedMatcherDecorator(
                new TelegramContactCallbackMatcher(botStateContainer),
                telegramAuthService
        );
        var handler = new TelegramContactCallbackHandler(telegramClient, botStateContainer);
        return new TelegramUpdateProcessor(
                matcher,
                handler
        );
    }

    @Bean
    TelegramUpdateProcessor contactSetFieldProcessor(BotStateContainer botStateContainer, TelegramClient telegramClient,
                                                     ContactMessageBuilder contactMessageBuilder, TelegramAuthService telegramAuthService) {
        var matcher = new TelegramAuthorizedMatcherDecorator(
                new TelegramContactSetFieldMatcher(botStateContainer),
                telegramAuthService
        );
        var handler = new TelegramContactSetFieldHandler(botStateContainer, telegramClient, contactMessageBuilder);
        return new TelegramUpdateProcessor(
                matcher,
                handler
        );
    }

    @Bean
    TelegramUpdateProcessor contactSaveContactProcessor(BotStateContainer botStateContainer, TelegramClient telegramClient,
                                                        PersonService personService, TelegramAuthService telegramAuthService) {
        var matcher = new TelegramAuthorizedMatcherDecorator(
                new TelegramSaveContactCallbackMatcher(botStateContainer),
                telegramAuthService
        );
        var handler = new TelegramSaveContactHandler(personService, botStateContainer, telegramClient, telegramAuthService);
        return new TelegramUpdateProcessor(
                matcher,
                handler
        );
    }

    @Bean
    TelegramUpdateProcessor logoutProcessor(BotStateContainer botStateContainer, TelegramClient telegramClient,
                                            TelegramAuthService telegramAuthService, TelegramSessionRepository telegramSessionRepository) {
        var matcher = new TelegramAuthorizedMatcherDecorator(
                new TelegramCommandMatcher("/logout"),
                telegramAuthService
        );
        var handler = new TelegramLogoutHandler(telegramSessionRepository, botStateContainer, telegramClient);
        return new TelegramUpdateProcessor(
                matcher,
                handler
        );
    }
}
