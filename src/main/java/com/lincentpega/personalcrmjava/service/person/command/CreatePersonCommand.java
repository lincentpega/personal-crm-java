package com.lincentpega.personalcrmjava.service.person.command;

import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import com.lincentpega.personalcrmjava.service.person.dto.create.CreateContactInfoDto;
import com.lincentpega.personalcrmjava.service.person.dto.create.CreateJobInfoDto;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Set;

public record CreatePersonCommand(
        long accountId,
        String firstName,
        @Nullable String middleName,
        @Nullable String lastName,
        PersonGender gender,
        @Nullable LocalDate birthDate,
        Set<CreateContactInfoDto> contactInfos,
        Set<CreateJobInfoDto> jobInfos
) {
}
