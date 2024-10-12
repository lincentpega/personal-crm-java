package com.lincentpega.personalcrmjava.controller.person.dto.request;

import org.springframework.validation.annotation.Validated;

@Validated
public record ContactInfoRequestElement(Long id, String method, String data) {
}
