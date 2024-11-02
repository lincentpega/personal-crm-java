package com.lincentpega.personalcrmjava.controller.person.mapper;

import com.lincentpega.personalcrmjava.controller.person.dto.request.CreateContactInfoRequestElement;
import com.lincentpega.personalcrmjava.controller.person.dto.request.CreateJobInfoRequestElement;
import com.lincentpega.personalcrmjava.controller.person.dto.request.UpdateContactInfoRequestElement;
import com.lincentpega.personalcrmjava.controller.person.dto.request.UpdateJobInfoRequestElement;
import com.lincentpega.personalcrmjava.service.person.dto.create.CreateContactInfoDto;
import com.lincentpega.personalcrmjava.service.person.dto.create.CreateJobInfoDto;
import com.lincentpega.personalcrmjava.service.person.dto.update.UpdateContactInfoDto;
import com.lincentpega.personalcrmjava.service.person.dto.update.UpdateJobInfoDto;

public class PersonRequestMapper {

    public static CreateJobInfoDto toCreateJobInfoDto(CreateJobInfoRequestElement jobInfoRequest) {
        return new CreateJobInfoDto(jobInfoRequest.company(), jobInfoRequest.position(), jobInfoRequest.isCurrent());
    }

    public static CreateContactInfoDto toCreateContactInfoDto(CreateContactInfoRequestElement contactInfoRequest) {
        return new CreateContactInfoDto(contactInfoRequest.method(), contactInfoRequest.data());
    }

    public static UpdateJobInfoDto toUpdateJobInfoDto(UpdateJobInfoRequestElement jobInfoRequest) {
        return new UpdateJobInfoDto(jobInfoRequest.id(), jobInfoRequest.company(), jobInfoRequest.position(), jobInfoRequest.isCurrent());
    }

    public static UpdateContactInfoDto toUpdateContactInfoDto(UpdateContactInfoRequestElement contactInfoRequest) {
        return new UpdateContactInfoDto(contactInfoRequest.id(), contactInfoRequest.method(), contactInfoRequest.data());
    }
}
