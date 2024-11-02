package com.lincentpega.personalcrmjava.service.security;

import com.lincentpega.personalcrmjava.data.RefreshTokenRepository;
import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.domain.account.RefreshToken;
import com.lincentpega.personalcrmjava.service.account.AccountService;
import com.lincentpega.personalcrmjava.service.security.command.LogInUserCommand;
import com.lincentpega.personalcrmjava.service.security.command.RegisterUserCommand;
import com.lincentpega.personalcrmjava.service.security.result.Tokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public Account signUp(RegisterUserCommand command) {
        var encodedPassword = passwordEncoder.encode(command.password());
        Account account = new Account(command.email(), encodedPassword, command.username());
        return accountService.save(account);
    }

    /**
     * @throws BadCredentialsException is thrown if invalid credentials provided
     */
    public Tokens logIn(LogInUserCommand command) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            command.username(),
                            command.password()
                    )
            );
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }

        var account = accountService.getAccountByUsername(command.username()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(account);
        var refreshTokenEntity = refreshTokenRepository.save(new RefreshToken(account));
        var refreshToken = jwtService.generateRefreshToken(refreshTokenEntity);

        return new Tokens(accessToken, refreshToken);
    }

    public Tokens refreshToken(String refreshToken) {
        var refreshTokenInfo = jwtService.extractRefreshTokenInfo(refreshToken);
        var existingRefreshTokenEntity = refreshTokenRepository.findById(refreshTokenInfo.id()).orElseThrow(() -> {
            log.warn("Refresh token does not exist");
            return new BadCredentialsException("Refresh token does not exist");
        });
        var username = refreshTokenInfo.username();
        var account = existingRefreshTokenEntity.getAccount();
        if (!Objects.equals(username, account.getUsername())) {
            throw new BadCredentialsException("Refresh token does not match");
        }
        var accessToken = jwtService.generateAccessToken(account);
        refreshTokenRepository.delete(existingRefreshTokenEntity);
        var newRefreshTokenEntity = refreshTokenRepository.save(new RefreshToken(account));
        var newRefreshToken = jwtService.generateRefreshToken(newRefreshTokenEntity);
        return new Tokens(accessToken, newRefreshToken);
    }
}
