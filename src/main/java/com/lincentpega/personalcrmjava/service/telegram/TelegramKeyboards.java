package com.lincentpega.personalcrmjava.service.telegram;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@UtilityClass
public class TelegramKeyboards {

    public static InlineKeyboardMarkup createContactKeyboard() {

        var setFirstNameButton = createButton("Set first name", TelegramContactCallbackValue.SET_FIRST_NAME);
        var setMiddleNameButton = createButton("Set middle name", TelegramContactCallbackValue.SET_MIDDLE_NAME);
        var setLastNameButton = createButton("Set last name", TelegramContactCallbackValue.SET_LAST_NAME);
        var setGenderButton = createButton("Set gender", TelegramContactCallbackValue.SET_GENDER);
        var setBirthdateButton = createButton("Set birthday", TelegramContactCallbackValue.SET_BIRHTDATE);
        var saveContactButton = createButton("Save contact", TelegramContactCallbackValue.SAVE_CONTACT);

        var keyboardRows = List.of(
                new InlineKeyboardRow(setFirstNameButton),
                new InlineKeyboardRow(setMiddleNameButton),
                new InlineKeyboardRow(setLastNameButton),
                new InlineKeyboardRow(setGenderButton),
                new InlineKeyboardRow(setBirthdateButton),
                new InlineKeyboardRow(saveContactButton)

        );
        return new InlineKeyboardMarkup(keyboardRows);
    }

    private static InlineKeyboardButton createButton(String text, TelegramContactCallbackValue callbackData) {
        var button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData.getCallbackData());
        return button;
    }
}
