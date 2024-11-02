package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.account.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
