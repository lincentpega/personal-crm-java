package com.lincentpega.personalcrmjava.service.telegram;

public enum TelegramBotState {
    INITIAL,
    CREATE_CONTACT,
    CONTACT_SET_FIRST_NAME,
    CONTACT_SET_MIDDLE_NAME,
    CONTACT_SET_LAST_NAME,
    CONTACT_SET_BIRTHDATE,
    CONTACT_SET_GENDER,
}
