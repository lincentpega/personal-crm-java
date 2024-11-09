package com.lincentpega.personalcrmjava.service.security.command;

public record RegisterUserCommand(String email, String password, String username) {
}
