package com.lincentpega.personalcrmjava.controller.security;

import com.lincentpega.personalcrmjava.controller.security.request.LogInUserRequest;
import com.lincentpega.personalcrmjava.controller.security.request.RefreshTokenRequest;
import com.lincentpega.personalcrmjava.controller.security.request.RegistersUserRequest;
import com.lincentpega.personalcrmjava.controller.security.response.AccountResponse;
import com.lincentpega.personalcrmjava.controller.security.response.LogInResponse;
import com.lincentpega.personalcrmjava.controller.security.response.RefreshTokenResponse;
import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.exception.EmailAlreadyExistsException;
import com.lincentpega.personalcrmjava.exception.PropertyValidationException;
import com.lincentpega.personalcrmjava.exception.UsernameAlreadyExistsException;
import com.lincentpega.personalcrmjava.service.security.AuthenticationService;
import com.lincentpega.personalcrmjava.service.security.command.LogInUserCommand;
import com.lincentpega.personalcrmjava.service.security.command.RegisterUserCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AccountResponse> signUp(@RequestBody @Validated RegistersUserRequest request) {
        Account account;
        try {
            account = authenticationService.signUp(new RegisterUserCommand(request.email(), request.password(), request.username()));
        } catch (UsernameAlreadyExistsException e) {
            throw new PropertyValidationException("username", "Username already exists");
        } catch (EmailAlreadyExistsException e) {
            throw new PropertyValidationException("email", "Email already exists");
        }

        return ResponseEntity.ok(new AccountResponse(account.getEmail(), account.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponse> logIn(@RequestBody @Validated LogInUserRequest request) {
        try {
            var tokens = authenticationService.logIn(new LogInUserCommand(request.username(), request.password()));
            return ResponseEntity.ok(new LogInResponse(tokens.accessToken(), tokens.refreshToken()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody @Validated RefreshTokenRequest request) {
        try {
            var tokens = authenticationService.refreshToken(request.token());
            return ResponseEntity.ok(new RefreshTokenResponse(tokens.accessToken(), tokens.refreshToken()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
