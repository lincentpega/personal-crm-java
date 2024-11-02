package com.lincentpega.personalcrmjava.controller.person;

import com.lincentpega.personalcrmjava.controller.common.PagedResponseWithTotalCount;
import com.lincentpega.personalcrmjava.controller.common.ValidationError;
import com.lincentpega.personalcrmjava.controller.person.dto.request.CreatePersonRequest;
import com.lincentpega.personalcrmjava.controller.person.dto.request.UpdatePersonRequest;
import com.lincentpega.personalcrmjava.controller.person.dto.response.FullPersonResponse;
import com.lincentpega.personalcrmjava.controller.person.dto.response.ShortPersonResponse;
import com.lincentpega.personalcrmjava.controller.person.mapper.PersonRequestMapper;
import com.lincentpega.personalcrmjava.controller.person.mapper.PersonResponsePresentor;
import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.domain.person.Person;
import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import com.lincentpega.personalcrmjava.exception.ValidationException;
import com.lincentpega.personalcrmjava.service.person.PersonService;
import com.lincentpega.personalcrmjava.service.person.command.CreatePersonCommand;
import com.lincentpega.personalcrmjava.service.person.command.UpdatePersonCommand;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private final PersonService personService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<PagedResponseWithTotalCount<ShortPersonResponse>> getPeople(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            Authentication authentication) {
        var account = (Account) authentication.getPrincipal();
        Page<Person> pagedPersons = personService.getPersonsWithOwner(account.getId(), pageNumber, pageSize);
        List<ShortPersonResponse> persons = pagedPersons.getContent().stream()
                .map(PersonResponsePresentor::presentShort)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new PagedResponseWithTotalCount<>(persons, pagedPersons.getTotalElements(), pagedPersons.getTotalPages(), pageNumber, pageSize)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullPersonResponse> getPersonById(
            @PathVariable Long id,
            Authentication authentication) {
        var account = (Account) authentication.getPrincipal();
        Person person = personService.getPersonByIdWithOwner(id, account.getId());
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PersonResponsePresentor.presentFull(person));
    }

    @PostMapping
    public ResponseEntity<FullPersonResponse> createPerson(
            @RequestBody @Validated CreatePersonRequest request,
            Authentication authentication) {
        LocalDate birthDate = null;
        if (request.birthDate() != null) {
            birthDate = parseDate(request.birthDate());
        }

        var account = (Account) authentication.getPrincipal();
        var command = new CreatePersonCommand(
                account.getId(),
                request.firstName(),
                request.middleName(),
                request.lastName(),
                PersonGender.valueOf(request.gender()),
                birthDate,
                request.contactInfos() != null
                        ? request.contactInfos().stream().map(PersonRequestMapper::toCreateContactInfoDto).collect(Collectors.toSet())
                        : null,
                request.jobInfos() != null
                        ? request.jobInfos().stream().map(PersonRequestMapper::toCreateJobInfoDto).collect(Collectors.toSet())
                        : null
        );
        Person createdPerson = personService.createPerson(command);
        return ResponseEntity.ok(PersonResponsePresentor.presentFull(createdPerson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FullPersonResponse> updatePerson(@RequestBody @Validated UpdatePersonRequest request) {
        LocalDate birthDate = null;
        if (request.birthDate() != null) {
            birthDate = parseDate(request.birthDate());
        }

        Person updatedPerson = personService.updatePerson(
                new UpdatePersonCommand(
                        request.id(),
                        request.firstName(),
                        request.middleName(),
                        request.lastName(),
                        birthDate,
                        request.contactInfos() != null
                                ? Arrays.stream(request.contactInfos()).map(PersonRequestMapper::toUpdateContactInfoDto).collect(Collectors.toSet())
                                : null,
                        request.jobInfos() != null
                                ? Arrays.stream(request.jobInfos()).map(PersonRequestMapper::toUpdateJobInfoDto).collect(Collectors.toSet())
                                : null
                )
        );

        return ResponseEntity.ok(PersonResponsePresentor.presentFull(updatedPerson));
    }

    private LocalDate parseDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            return LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new ValidationException(
                    Collections.singletonList(new ValidationError("birthDate", "Invalid date format"))
            );
        }
    }
}
