package com.lincentpega.personalcrmjava.service.notification;

import lombok.Data;

@Data
public class BirthdayNotificationInfo {
    private final String firstName;
    private final String lastName;
    private final String birthday;
}
