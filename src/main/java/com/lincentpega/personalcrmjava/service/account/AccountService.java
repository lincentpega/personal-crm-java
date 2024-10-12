package com.lincentpega.personalcrmjava.service.account;

import com.lincentpega.personalcrmjava.data.AccountRepository;
import com.lincentpega.personalcrmjava.domain.account.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public boolean existsByName(String name) {
        return accountRepository.existsByName(name);
    }
}
