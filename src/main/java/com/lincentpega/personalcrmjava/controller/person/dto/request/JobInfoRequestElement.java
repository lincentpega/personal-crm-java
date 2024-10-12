package com.lincentpega.personalcrmjava.controller.person.dto.request;

import org.springframework.validation.annotation.Validated;

@Validated
public record JobInfoRequestElement(
    Long id,
    String company,
    String position,
    boolean isCurrent
) {}
