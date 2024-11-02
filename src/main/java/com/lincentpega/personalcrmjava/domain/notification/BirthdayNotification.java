package com.lincentpega.personalcrmjava.domain.notification;

import com.lincentpega.personalcrmjava.domain.person.Person;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "birthday_notifications", indexes = {
        @Index(name = "idx_notification_status", columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class BirthdayNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false, referencedColumnName = "id")
    private Person person;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    public BirthdayNotification(String content, Person person, NotificationStatus status) {
        this.content = content;
        this.person = person;
        this.status = status;
    }


}
