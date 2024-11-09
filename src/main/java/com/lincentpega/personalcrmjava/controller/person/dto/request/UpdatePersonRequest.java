package com.lincentpega.personalcrmjava.controller.person.dto.request;

import com.lincentpega.personalcrmjava.controller.validator.ValidDate;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

@Validated
public record UpdatePersonRequest(
        @NonNull Long accountId,
        @NotEmpty String firstName,
        @Nullable String middleName,
        @Nullable String lastName,
        @Nullable @ValidDate String birthDate,
        @Nullable UpdateContactInfoRequestElement[] contactInfos,
        @Nullable UpdateJobInfoRequestElement[] jobInfos
) {
}
