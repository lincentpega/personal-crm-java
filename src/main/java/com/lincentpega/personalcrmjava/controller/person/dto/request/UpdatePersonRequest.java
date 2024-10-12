package com.lincentpega.personalcrmjava.controller.person.dto.request;

import org.springframework.validation.annotation.Validated;

@Validated
public record UpdatePersonRequest(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    String birthDate,
    ContactInfoRequestElement[] contactInfos,
    JobInfoRequestElement[] jobInfos
) {}
