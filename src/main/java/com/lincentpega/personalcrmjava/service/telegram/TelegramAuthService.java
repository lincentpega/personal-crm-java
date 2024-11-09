package com.lincentpega.personalcrmjava.service.telegram;

import com.lincentpega.personalcrmjava.data.TelegramSessionRepository;
import com.lincentpega.personalcrmjava.domain.TelegramSession;
import com.lincentpega.personalcrmjava.domain.account.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TelegramAuthService {

    private final TelegramSessionRepository sessionRepository;

    public TelegramAuthService(TelegramSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public boolean isAuthorized(String chatId) {
        return sessionRepository.findByChatId(chatId).isPresent();
    }

    public Optional<Account> getAuthorizedAccount(String chatId) {
        Optional<TelegramSession> telegramSession = sessionRepository.findByChatId(chatId);
        return telegramSession.map(TelegramSession::getAccount);
    }

    public List<String> getAccountsChatIds(long accountId) {
        return sessionRepository.findAllByAccount_Id(accountId).stream()
                .map(TelegramSession::getChatId)
                .filter(Objects::nonNull)
                .toList();
    }
}
