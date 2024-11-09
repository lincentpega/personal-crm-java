package com.lincentpega.personalcrmjava.service.telegram;

import lombok.Getter;

@Getter
public enum TelegramFieldName {
    FIRST_NAME("first-name"),
    MIDDLE_NAME("middle-name"),
    LAST_NAME("last-name"),
    BIRTH_DATE("birthdate"),
    GENDER("gender");

    private final String name;

    TelegramFieldName(String name) {
        this.name = name;
    }
}
