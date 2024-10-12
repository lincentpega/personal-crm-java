package com.lincentpega.personalcrmjava.controller.person;

import com.lincentpega.personalcrmjava.controller.person.dto.response.ContactInfoResponseElement;
import com.lincentpega.personalcrmjava.controller.person.dto.response.FullPersonResponse;
import com.lincentpega.personalcrmjava.controller.person.dto.response.JobInfoResponseElement;
import com.lincentpega.personalcrmjava.controller.person.dto.response.ShortPersonResponse;
import com.lincentpega.personalcrmjava.domain.person.Person;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class PersonResponsePresentor {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ShortPersonResponse presentShort(Person person) {
        return new ShortPersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getMiddleName(),
                person.getLastName(),
                person.getBirthDate() != null ? person.getBirthDate().format(dateFormatter) : null,
                person.getGender()
        );
    }

    public FullPersonResponse presentFull(Person person) {
        return new FullPersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getMiddleName(),
                person.getLastName(),
                person.getBirthDate() != null ? person.getBirthDate().format(dateFormatter) : null,
                person.getGender() != null ? person.getGender().toString().toLowerCase() : null,
                person.getContactInfos().stream()
                        .map(it -> new ContactInfoResponseElement(
                                it.getMethod(),
                                it.getData()
                        ))
                        .collect(Collectors.toList()),
                person.getJobInfos().stream()
                        .map(it -> new JobInfoResponseElement(
                                it.getCompany(),
                                it.getPosition(),
                                it.isCurrent()
                        ))
                        .collect(Collectors.toList())
        );
    }
}