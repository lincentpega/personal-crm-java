package com.lincentpega.personalcrmjava.service.notification;

public interface BirthdayNotificationGateway<T extends BirthdayNotificationInfo> {
    void send(T notificationInfo);
}