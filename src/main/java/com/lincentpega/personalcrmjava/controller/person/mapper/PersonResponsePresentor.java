package com.lincentpega.personalcrmjava.controller.person.mapper;

import com.lincentpega.personalcrmjava.controller.person.dto.response.ContactInfoResponseElement;
import com.lincentpega.personalcrmjava.controller.person.dto.response.FullPersonResponse;
import com.lincentpega.personalcrmjava.controller.person.dto.response.JobInfoResponseElement;
import com.lincentpega.personalcrmjava.controller.person.dto.response.ShortPersonResponse;
import com.lincentpega.personalcrmjava.domain.person.Person;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class PersonResponsePresentor {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ShortPersonResponse presentShort(Person person) {
        return new ShortPersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getMiddleName(),
                person.getLastName(),
                person.getBirthDate() != null ? person.getBirthDate().format(dateFormatter) : null,
                person.getGender()
        );
    }

    public static FullPersonResponse presentFull(Person person) {
        return new FullPersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getMiddleName(),
                person.getLastName(),
                person.getBirthDate() != null ? person.getBirthDate().format(dateFormatter) : null,
                person.getGender() != null ? person.getGender().toString() : null,
                person.getContactInfos().stream()
                        .sorted((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()))
                        .map(it -> new ContactInfoResponseElement(
                                it.getMethod(),
                                it.getData()
                        ))
                        .collect(Collectors.toList()),
                person.getJobInfos().stream()
                        .sorted((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()))
                        .map(it -> new JobInfoResponseElement(
                                it.getCompany(),
                                it.getPosition(),
                                it.isCurrent()
                        ))
                        .collect(Collectors.toList())
        );
    }
}