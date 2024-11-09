package com.lincentpega.personalcrmjava.service.notification;

import com.lincentpega.personalcrmjava.data.BirthdayNotificationRepository;
import com.lincentpega.personalcrmjava.data.PersonRepository;
import com.lincentpega.personalcrmjava.domain.notification.BirthdayNotification;
import com.lincentpega.personalcrmjava.domain.notification.NotificationStatus;
import com.lincentpega.personalcrmjava.service.account.AccountService;
import com.lincentpega.personalcrmjava.service.telegram.TelegramAuthService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Log4j2
@Component
public class BirthdayNotificationScheduler {

    private final BirthdayNotificationRepository notificationRepository;
    private final PersonRepository personRepository;
    private final TelegramClient telegramClient;
    private final StringRedisTemplate redisTemplate;
    private final AccountService accountService;
    private final TelegramAuthService telegramAuthService;

    public BirthdayNotificationScheduler(
            BirthdayNotificationRepository birthdayNotificationRepository,
            PersonRepository personRepository,
            TelegramClient telegramClient,
            StringRedisTemplate stringRedisTemplate,
            AccountService accountService,
            TelegramAuthService telegramAuthService) {
        this.notificationRepository = birthdayNotificationRepository;
        this.personRepository = personRepository;
        this.telegramClient = telegramClient;
        this.redisTemplate = stringRedisTemplate;
        this.accountService = accountService;
        this.telegramAuthService = telegramAuthService;
    }

    @Scheduled(cron = "0 0 10-23 * * *", zone = "Europe/Moscow")
    public void createNotifications() {
        var tzId = "Europe/Moscow";
        TimeZone timeZone = TimeZone.getTimeZone(tzId);
        var date = LocalDate.now(timeZone.toZoneId());
        var processedKey = "birthday-processed:" + date.format(DateTimeFormatter.ISO_DATE);
        var processed = Boolean.TRUE.equals(redisTemplate.hasKey(processedKey));
        if (processed) {
            return;
        }

        var today = LocalDate.now(timeZone.toZoneId());

        accountService.getAccounts().forEach(account ->
                personRepository.getPeopleWithBirthday(account.getId(), today).forEach(person -> {
                    if (notificationRepository.existsForPersonToday(person.getId(), tzId)) {
                        return;
                    }

                    var firstName = person.getFirstName();
                    var lastName = person.getLastName();

                    StringBuilder messageBuilder = new StringBuilder();
                    messageBuilder.append("У ").append(firstName);
                    if (lastName != null) {
                        messageBuilder.append(" ").append(lastName);
                    }
                    messageBuilder.append(" сегодня день рождения\nНе забудь его поздравить");

                    var notification = new BirthdayNotification(messageBuilder.toString(), person, NotificationStatus.PENDING);
                    notificationRepository.save(notification);
                }));

        redisTemplate.opsForSet().add(processedKey, "true");
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void processNotifications() {
        notificationRepository.findByStatus(NotificationStatus.PENDING).forEach(notification -> {
            try {
                var person = notification.getPerson();
                if (!person.getSettings().isNotifyOnBirthday()) {
                    return;
                }
                var content = notification.getContent();
                boolean sent = false;
                for (String chatId : telegramAuthService.getAccountsChatIds(person.getAccount().getId())) {
                    var response = new SendMessage(chatId, content);
                    try {
                        telegramClient.execute(response);
                        sent = true;
                    } catch (TelegramApiException e) {
                        log.error("Failed to send telegram update", e);
                    }
                }
                if (!sent) {
                    notification.setStatus(NotificationStatus.FAILURE);
                } else {
                    notification.setStatus(NotificationStatus.SUCCESS);
                }
                notificationRepository.save(notification);
            } catch (Exception e) {
                log.error("Issue while processing notification", e);
            }
        });
    }
}
