package com.lincentpega.personalcrmjava.service.telegram.handler;

import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.service.account.AccountService;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateHandlerFunc;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Log4j2
public class TelegramStartHandler implements TelegramUpdateHandlerFunc {

    private final TelegramClient telegramClient;
    private final AccountService accountService;

    public TelegramStartHandler(ApplicationContext applicationContext) {
        this.telegramClient = applicationContext.getBean(TelegramClient.class);
        this.accountService = applicationContext.getBean(AccountService.class);
    }

    @Override
    public void handle(Update update) {
        try {
            var message = update.getMessage();
            var chatId = message.getChatId().toString();
            var username = message.getFrom().getUserName();

            if (!accountService.existsByName(username)) {
                accountService.save(new Account(username, chatId));
            }

            var response = new SendMessage(chatId, "Добро пожаловать в Personal CRM bot");
            telegramClient.execute(response);
        } catch (TelegramApiException e) {
            log.error("Failed to send update to telegram", e);
        }
    }
}
