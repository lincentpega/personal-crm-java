package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByName(String name);
}
