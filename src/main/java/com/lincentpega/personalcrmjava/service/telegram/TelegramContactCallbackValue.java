package com.lincentpega.personalcrmjava.service.telegram;

import lombok.Getter;

@Getter
public enum TelegramContactCallbackValue {

    SET_FIRST_NAME("#create_contact:set_first_name"),
    SET_MIDDLE_NAME("#create_contact:set_middle_name"),
    SET_LAST_NAME("#create_contact:set_last_name"),
    SET_BIRHTDATE("#create_contact:set_birthdate"),
    SET_GENDER("#create_contact:set_gender"),
    SAVE_CONTACT("#save_contact");

    private final String callbackData;

    TelegramContactCallbackValue(String callbackData) {
        this.callbackData = callbackData;
    }
}
