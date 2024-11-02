package com.lincentpega.personalcrmjava.controller.person.dto.request;

import com.lincentpega.personalcrmjava.controller.validator.ValidDate;
import com.lincentpega.personalcrmjava.controller.validator.ValidGender;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record CreatePersonRequest(
        @NotEmpty @Size(max = 50) String firstName,
        @Nullable @Size(max = 50) String middleName,
        @Nullable @Size(max = 50) String lastName,
        @Nullable @ValidGender String gender,
        @Nullable @ValidDate String birthDate,
        @Nullable Set<CreateJobInfoRequestElement> jobInfos,
        @Nullable Set<CreateContactInfoRequestElement> contactInfos
) {
}
