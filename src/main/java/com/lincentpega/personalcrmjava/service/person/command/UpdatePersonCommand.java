package com.lincentpega.personalcrmjava.service.person.command;

import com.lincentpega.personalcrmjava.service.person.dto.update.UpdateContactInfoDto;
import com.lincentpega.personalcrmjava.service.person.dto.update.UpdateJobInfoDto;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.Set;

public record UpdatePersonCommand(
        Long id,
        String firstName,
        String middleName,
        String lastName,
        LocalDate birthDate,
        @Nullable Set<UpdateContactInfoDto> contactInfos,
        @Nullable Set<UpdateJobInfoDto> jobInfos
) {
}
