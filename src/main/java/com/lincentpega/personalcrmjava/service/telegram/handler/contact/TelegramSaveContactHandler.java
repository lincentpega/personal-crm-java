package com.lincentpega.personalcrmjava.service.telegram.handler.contact;

import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import com.lincentpega.personalcrmjava.service.person.PersonService;
import com.lincentpega.personalcrmjava.service.telegram.BotStateContainer;
import com.lincentpega.personalcrmjava.service.telegram.TelegramBotState;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUpdateHandlerFunc;
import com.lincentpega.personalcrmjava.service.telegram.TelegramUtils;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Log4j2
public class TelegramSaveContactHandler implements TelegramUpdateHandlerFunc {

    private final PersonService personService;
    private final BotStateContainer botStateContainer;
    private final TelegramClient telegramClient;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TelegramSaveContactHandler(ApplicationContext applicationContext) {
        this.personService = applicationContext.getBean(PersonService.class);
        this.botStateContainer = applicationContext.getBean(BotStateContainer.class);
        this.telegramClient = applicationContext.getBean(TelegramClient.class);
    }

    @SneakyThrows
    @Override
    public void handle(Update update) {
        var chatId = TelegramUtils.getChatIdNonNull(update);
        var firstName = botStateContainer.getValue(chatId, "first-name");
        if (firstName == null) {
            telegramClient.execute(new SendMessage(chatId, "First Name must not be empty"));
            return;
        }
        var middleName = botStateContainer.getValue(chatId, "middle-name");
        var lastName = botStateContainer.getValue(chatId, "last-name");
        var genderRaw = botStateContainer.getValue(chatId, "gender");
        var gender = genderRaw != null ? PersonGender.valueOf(genderRaw) : null;
        var birthdateRaw = botStateContainer.getValue(chatId, "birthdate");
        var birthdate = birthdateRaw != null ? parseDate(birthdateRaw) : null;
//        personService.createPerson(new CreatePersonCommand(firstName, middleName, lastName, gender, birthdate));
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
