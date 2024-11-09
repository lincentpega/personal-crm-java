package com.lincentpega.personalcrmjava.service.telegram.handler;

import com.lincentpega.personalcrmjava.data.TelegramSessionRepository;
import com.lincentpega.personalcrmjava.domain.TelegramSession;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateHandlerFunc;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
public class TelegramStartHandler implements TelegramUpdateHandlerFunc {

    private final TelegramClient telegramClient;
    private final TelegramSessionRepository telegramSessionRepository;

    public TelegramStartHandler(TelegramClient telegramClient, TelegramSessionRepository telegramSessionRepository) {
        this.telegramClient = telegramClient;
        this.telegramSessionRepository = telegramSessionRepository;
    }

    @SneakyThrows
    @Override
    public void handle(Update update) {
        var message = update.getMessage();
        var chatId = message.getChatId().toString();
        var text = message.getText();
        var args = getCommandArgs(text);

        if (args.isEmpty()) {
            sendMessage(chatId, "Добро пожаловать в Personal CRM bot. Авторизуйтесь на сайте http");
            return;
        }

        if (args.size() > 1) {
            sendMessage(chatId, "Некорректная ссылка, перейдите по ссылке авторизации с сайта http://lincentpega.com");
            return;
        }

        String sessionIdRaw = args.getFirst();
        UUID sessionId = parseSessionId(sessionIdRaw);
        if (sessionId == null) {
            sendMessage(chatId, "Некорректная ссылка, перейдите по ссылке авторизации с сайта http://lincentpega.com");
            return;
        }

        Optional<TelegramSession> telegramSessionOpt = telegramSessionRepository.findById(sessionId);
        if (telegramSessionOpt.isEmpty()) {
            sendMessage(chatId, "Некорректная ссылка, перейдите по ссылке авторизации с сайта http://lincentpega.com");
            return;
        }

        TelegramSession telegramSession = telegramSessionOpt.get();
        if (telegramSession.getChatId() != null) {
            sendMessage(chatId, "Авторизация уже выполнена на другом аккаунте");
            return;
        }

        telegramSession.setChatId(chatId);
        telegramSessionRepository.save(telegramSession);
    }

    private UUID parseSessionId(String sessionIdRaw) {
        try {
            return UUID.fromString(sessionIdRaw);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @SneakyThrows
    private void sendMessage(String chatId, String text) {
        var response = new SendMessage(chatId, text);
        telegramClient.execute(response);
    }

    private static List<String> getCommandArgs(String text) {
        var commandWithArgs = text.split(" ");
        if (commandWithArgs.length < 2) {
            return List.of();
        }
        return Arrays.asList(commandWithArgs).subList(1, text.length());
    }
}
