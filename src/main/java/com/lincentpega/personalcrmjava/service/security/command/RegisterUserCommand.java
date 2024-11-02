package com.lincentpega.personalcrmjava.service.security.command;

import lombok.Data;

public record RegisterUserCommand(String email, String password, String username) {
}
