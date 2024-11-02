package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Account> findByUsername(String username);

    @Query(value = "SELECT locale FROM accounts WHERE id = :id", nativeQuery = true)
    String getLocaleById(long id);
}
