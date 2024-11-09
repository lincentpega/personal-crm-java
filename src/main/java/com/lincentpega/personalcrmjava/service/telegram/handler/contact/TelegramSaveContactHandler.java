package com.lincentpega.personalcrmjava.service.telegram.handler.contact;

import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import com.lincentpega.personalcrmjava.service.person.PersonService;
import com.lincentpega.personalcrmjava.service.person.command.CreatePersonCommand;
import com.lincentpega.personalcrmjava.service.telegram.*;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import static com.lincentpega.personalcrmjava.service.telegram.TelegramFieldName.*;

@Log4j2
public class TelegramSaveContactHandler implements TelegramUpdateHandlerFunc {

    private final PersonService personService;
    private final BotStateContainer botStateContainer;
    private final TelegramClient telegramClient;
    private final TelegramAuthService authService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TelegramSaveContactHandler(PersonService personService,
                                      BotStateContainer botStateContainer,
                                      TelegramClient telegramClient,
                                      TelegramAuthService authService) {
        this.personService = personService;
        this.botStateContainer = botStateContainer;
        this.telegramClient = telegramClient;
        this.authService = authService;
    }

    @SneakyThrows
    @Override
    public void handle(Update update) {
        var chatId = TelegramUtils.getChatIdNonNull(update);
        Account authorizedAccount = authService.getAuthorizedAccount(chatId)
                .orElseThrow(() -> new IllegalStateException("Failed to retrieve authorized account"));
        var firstName = botStateContainer.getValue(chatId, FIRST_NAME.getName());
        if (firstName == null) {
            telegramClient.execute(new SendMessage(chatId, "First Name must not be empty"));
            return;
        }
        var middleName = botStateContainer.getValue(chatId, MIDDLE_NAME.getName());
        var lastName = botStateContainer.getValue(chatId, LAST_NAME.getName());
        var genderRaw = botStateContainer.getValue(chatId, GENDER.getName());
        var gender = genderRaw != null ? PersonGender.valueOf(genderRaw) : null;
        var birthdateRaw = botStateContainer.getValue(chatId, BIRTH_DATE.getName());
        var birthdate = birthdateRaw != null ? parseDate(birthdateRaw) : null;
        personService.createPerson(new CreatePersonCommand(authorizedAccount.getId(), firstName, middleName, lastName, gender, birthdate, Set.of(), Set.of()));
        botStateContainer.clearValues(chatId);
        botStateContainer.setState(chatId, TelegramBotState.INITIAL);
        var response = new EditMessageText("Contact saved successfully");
        response.setChatId(chatId);
        response.setReplyMarkup(null);
        response.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        telegramClient.execute(response);
    }

    private LocalDate parseDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            return LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            log.error(e);
            return null;
        }
    }
}
