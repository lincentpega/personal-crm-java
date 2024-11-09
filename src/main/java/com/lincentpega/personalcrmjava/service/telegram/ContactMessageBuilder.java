package com.lincentpega.personalcrmjava.service.telegram;

import org.springframework.stereotype.Service;

@Service
public class ContactMessageBuilder {

    private final BotStateContainer botStateContainer;

    public ContactMessageBuilder(BotStateContainer botStateContainer) {
        this.botStateContainer = botStateContainer;
    }

    public String buildMessage(String chatId) {
        var firstName = botStateContainer.getValue(chatId, TelegramFieldName.FIRST_NAME.getName());
        var middleName = botStateContainer.getValue(chatId, TelegramFieldName.MIDDLE_NAME.getName());
        var lastName = botStateContainer.getValue(chatId, TelegramFieldName.LAST_NAME.getName());
        var gender = botStateContainer.getValue(chatId, TelegramFieldName.GENDER.getName());
        var birthdate = botStateContainer.getValue(chatId, TelegramFieldName.BIRTH_DATE.getName());

        return String.format("Current person data:\nFirst Name: %s\nMiddle Name: %s\nLast Name: %s\nGender: %s\nBirthdate: %s",
                firstName, middleName, lastName, gender, birthdate);
    }
}
