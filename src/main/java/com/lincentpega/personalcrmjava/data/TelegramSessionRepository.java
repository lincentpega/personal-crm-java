package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.TelegramSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TelegramSessionRepository extends JpaRepository<TelegramSession, UUID> {

    Optional<TelegramSession> findByChatId(String chatId);

    List<TelegramSession> findAllByAccount_Id(long accountId);
}
