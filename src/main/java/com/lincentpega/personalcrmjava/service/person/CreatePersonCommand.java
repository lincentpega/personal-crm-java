package com.lincentpega.personalcrmjava.service.person;

import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record CreatePersonCommand(
    String firstName,
    @Nullable String middleName,
    @Nullable String lastName,
    PersonGender gender,
    @Nullable LocalDate birthDate
) {
}
