package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.notification.BirthdayNotification;
import com.lincentpega.personalcrmjava.domain.notification.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BirthdayNotificationRepository extends JpaRepository<BirthdayNotification, Long> {

    Iterable<BirthdayNotification> findByStatus(NotificationStatus status);

    @Query(value = "SELECT COUNT(*) > 0 " +
            "FROM birthday_notifications bn " +
            "WHERE bn.person_id = :personId " +
            "AND DATE_PART('MONTH', bn.created_at AT TIME ZONE :timezone) = DATE_PART('MONTH', CURRENT_TIMESTAMP AT TIME ZONE :timezone) " +
            "AND DATE_PART('DAY', bn.created_at AT TIME ZONE :timezone) = DATE_PART('DAY', CURRENT_TIMESTAMP AT TIME ZONE :timezone)",
            nativeQuery = true)
    boolean existsForPersonToday(@Param("personId") long personId, @Param("timezone") String timezone);
}
