package com.lincentpega.personalcrmjava.controller.person.dto.request;

import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

@Validated
public record CreatePersonRequest(
        @NotEmpty String firstName,
        @Nullable String middleName,
        @Nullable String lastName,
        PersonGender gender,
        @Nullable String birthDate
) {
}
