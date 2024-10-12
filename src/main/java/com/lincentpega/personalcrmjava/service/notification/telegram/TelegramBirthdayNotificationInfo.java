package com.lincentpega.personalcrmjava.service.notification.telegram;

import com.lincentpega.personalcrmjava.service.notification.BirthdayNotificationInfo;
import lombok.Getter;

@Getter
public class TelegramBirthdayNotificationInfo extends BirthdayNotificationInfo {

    private final Long chatId;

    public TelegramBirthdayNotificationInfo(Long chatId, String firstName, String lastName, String birthday) {
        super(firstName, lastName, birthday);
        this.chatId = chatId;
    }
}
