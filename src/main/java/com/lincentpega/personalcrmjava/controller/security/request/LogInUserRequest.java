package com.lincentpega.personalcrmjava.controller.security.request;

import jakarta.validation.constraints.NotEmpty;

public record LogInUserRequest(@NotEmpty String username, @NotEmpty String password) {
}
