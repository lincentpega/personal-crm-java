package com.lincentpega.personalcrmjava.controller.person.dto.response;

public record FullPersonResponse(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    String birthDate,
    String gender,
    Iterable<ContactInfoResponseElement> contactInfos,
    Iterable<JobInfoResponseElement> jobInfos
) {}
