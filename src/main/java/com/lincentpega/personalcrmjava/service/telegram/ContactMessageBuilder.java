package com.lincentpega.personalcrmjava.service.telegram;

import org.springframework.stereotype.Service;

@Service
public class ContactMessageBuilder {

    private final BotStateContainer botStateContainer;

    public ContactMessageBuilder(BotStateContainer botStateContainer) {
        this.botStateContainer = botStateContainer;
    }

    public String buildMessage(String chatId) {
        var firstName = botStateContainer.getValue(chatId, "first-name");
        var middleName = botStateContainer.getValue(chatId, "middle-name");
        var lastName = botStateContainer.getValue(chatId, "last-name");
        var gender = botStateContainer.getValue(chatId, "gender");
        var birthdate = botStateContainer.getValue(chatId, "birthdate");

        return String.format("Current person data:\nFirst Name: %s\nMiddle Name: %s\nLast Name: %s\nGender: %s\nBirthdate: %s",
                firstName, lastName, middleName, gender, birthdate);
    }
}
