package com.lincentpega.personalcrmjava.service.account;

import com.lincentpega.personalcrmjava.data.AccountRepository;
import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.exception.EmailAlreadyExistsException;
import com.lincentpega.personalcrmjava.exception.UsernameAlreadyExistsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Log4j2
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account save(Account account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        return accountRepository.save(account);
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Nullable
    public Locale getAccountLocale(long accountId) {
        String localeRaw = accountRepository.getLocaleById(accountId);
        if (localeRaw == null) {
            return null;
        }
        Locale locale = Locale.of(localeRaw);
        if (locale == null) {
            log.error("Invalid account locale: {}", localeRaw);
            return null;
        }
        return locale;
    }
}
