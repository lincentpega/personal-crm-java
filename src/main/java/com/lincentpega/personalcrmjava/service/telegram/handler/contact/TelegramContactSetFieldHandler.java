package com.lincentpega.personalcrmjava.service.telegram.handler.contact;

import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import com.lincentpega.personalcrmjava.service.telegram.*;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

@Log4j2
public class TelegramContactSetFieldHandler implements TelegramUpdateHandlerFunc {

    private final BotStateContainer botStateContainer;
    private final TelegramClient telegramClient;
    private final ContactMessageBuilder contactMessageBuilder;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TelegramContactSetFieldHandler(ApplicationContext applicationContext) {
        this.botStateContainer = applicationContext.getBean(BotStateContainer.class);
        this.telegramClient = applicationContext.getBean(TelegramClient.class);
        this.contactMessageBuilder = applicationContext.getBean(ContactMessageBuilder.class);
    }

    @SneakyThrows
    @Override
    public void handle(Update update) {
        var chatId = TelegramUtils.getChatIdNonNull(update);
        var state = botStateContainer.getState(chatId);
        var message = update.getMessage().getText();

        if (message.isBlank()) {
            telegramClient.execute(new SendMessage(chatId, "Value must not be blank"));
        }
        if (state.equals(TelegramBotState.CONTACT_SET_FIRST_NAME)) {
            botStateContainer.setValue(chatId, "first-name", message);
        } else if (state.equals(TelegramBotState.CONTACT_SET_MIDDLE_NAME)) {
            botStateContainer.setValue(chatId, "middle-name", message);
        } else if (state.equals(TelegramBotState.CONTACT_SET_LAST_NAME)) {
            botStateContainer.setValue(chatId, "last-name", message);
        } else if (state.equals(TelegramBotState.CONTACT_SET_BIRTHDATE)) {
            var date = parseDate(message);
            if (date == null) {
                telegramClient.execute(new SendMessage(chatId, "Invalid date format. Should be dd-MM-yy"));
                return;
            }
            botStateContainer.setValue(chatId, "birthdate", message);
        } else if (state.equals(TelegramBotState.CONTACT_SET_GENDER)) {
            PersonGender personGender;
            try {
                personGender = PersonGender.valueOf(message);
            } catch (IllegalArgumentException e) {
                String genders = String.join(", ", Arrays.stream(PersonGender.values()).map(Enum::name).toArray(String[]::new));
                telegramClient.execute(new SendMessage(chatId, "Invalid gender. Allowed values: " + genders));
                return;
            }
            botStateContainer.setValue(chatId, "gender", personGender.toString());
        }

        InlineKeyboardMarkup keyboardMarkup = TelegramKeyboards.createContactKeyboard();
        var response = new SendMessage(chatId, contactMessageBuilder.buildMessage(chatId));
        response.setReplyMarkup(keyboardMarkup);
        botStateContainer.setState(chatId, TelegramBotState.CREATE_CONTACT);
        telegramClient.execute(response);
    }

    @SneakyThrows
    private LocalDate parseDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            return LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            log.error(e);
        }
        return null;
    }
}
