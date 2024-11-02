package com.lincentpega.personalcrmjava.controller.security.request;

import jakarta.validation.constraints.NotEmpty;

public record RegistersUserRequest(@NotEmpty String email, @NotEmpty String password, @NotEmpty String username) {
}
