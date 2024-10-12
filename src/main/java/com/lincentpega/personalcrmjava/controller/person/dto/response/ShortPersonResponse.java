package com.lincentpega.personalcrmjava.controller.person.dto.response;

import com.lincentpega.personalcrmjava.domain.person.PersonGender;

public record ShortPersonResponse(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    String birthDate,
    PersonGender gender
) {}
