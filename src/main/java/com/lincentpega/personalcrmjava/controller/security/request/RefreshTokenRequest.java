package com.lincentpega.personalcrmjava.controller.security.request;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequest(@NotEmpty String token) {
}
