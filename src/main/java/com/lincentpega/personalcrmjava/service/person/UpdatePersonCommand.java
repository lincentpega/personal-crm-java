package com.lincentpega.personalcrmjava.service.person;

import java.time.LocalDate;

public record UpdatePersonCommand(
    Long id,
    String firstName,
    String middleName,
    String lastName,
    LocalDate birthDate
) {
}
