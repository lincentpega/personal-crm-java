package com.lincentpega.personalcrmjava.service.telegram;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@UtilityClass
public class TelegramUtils {

    public static Optional<String> getChatId(Update update) {
        Long chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            return Optional.empty();
        }
        return Optional.of(chatId.toString());
    }

    public static String getChatIdNonNull(Update update) {
        Optional<String> chatId = getChatId(update);
        if (chatId.isEmpty()) {
            throw new IllegalStateException("Chat id is missing from update");
        }
        return chatId.get();
    }
}
