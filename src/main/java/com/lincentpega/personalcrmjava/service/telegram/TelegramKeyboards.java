package com.lincentpega.personalcrmjava.service.telegram;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@UtilityClass
public class TelegramKeyboards {

    public static InlineKeyboardMarkup createContactKeyboard() {
        var setFirstNameButton = createButton("Set first name", TelegramCallbackValue.SET_FIRST_NAME);
        var setMiddleNameButton = createButton("Set middle name", TelegramCallbackValue.SET_MIDDLE_NAME);
        var setLastNameButton = createButton("Set last name", TelegramCallbackValue.SET_LAST_NAME);
        var setBirthdateButton = createButton("Set birthdate", TelegramCallbackValue.SET_BIRHTDATE);
        var setGenderButton = createButton("Set gender", TelegramCallbackValue.SET_GENDER);

        var keyboardRows = List.of(
                new InlineKeyboardRow(setFirstNameButton),
                new InlineKeyboardRow(setMiddleNameButton),
                new InlineKeyboardRow(setLastNameButton),
                new InlineKeyboardRow(setBirthdateButton),
                new InlineKeyboardRow(setGenderButton)

        );
        return new InlineKeyboardMarkup(keyboardRows);
    }

    private static InlineKeyboardButton createButton(String text, TelegramCallbackValue callbackData) {
        var button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData.getCallbackData());
        return button;
    }
}
