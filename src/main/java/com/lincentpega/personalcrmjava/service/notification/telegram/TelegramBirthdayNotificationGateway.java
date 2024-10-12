package com.lincentpega.personalcrmjava.service.notification.telegram;

import com.lincentpega.personalcrmjava.service.notification.BirthdayNotificationGateway;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class TelegramBirthdayNotificationGateway implements BirthdayNotificationGateway<TelegramBirthdayNotificationInfo> {

    private final TelegramClient telegramClient;

    public TelegramBirthdayNotificationGateway(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @SneakyThrows
    @Override
    public void send(TelegramBirthdayNotificationInfo notificationInfo) {
        telegramClient.execute(
                new SendMessage(
                        notificationInfo.getChatId().toString(),
                        "У " + notificationInfo.getFirstName() + " " + notificationInfo.getLastName() + " сегодня день рождения. Не забудь поздравить его"
                )
        );
    }
}