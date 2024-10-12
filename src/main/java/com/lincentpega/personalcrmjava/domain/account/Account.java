package com.lincentpega.personalcrmjava.domain.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id = 0;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "chat_id", nullable = false)
    private String chatId;

    public Account(String name, String chatId) {
        this.name = name;
        this.chatId = chatId;
    }

    public Account(long id) {
        this.id = id;
    }
}
