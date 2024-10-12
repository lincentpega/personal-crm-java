package com.lincentpega.personalcrmjava.controller.person;

import com.lincentpega.personalcrmjava.controller.common.PagedResponseWithTotalCount;
import com.lincentpega.personalcrmjava.controller.common.ValidationError;
import com.lincentpega.personalcrmjava.controller.person.dto.request.CreatePersonRequest;
import com.lincentpega.personalcrmjava.controller.person.dto.request.UpdatePersonRequest;
import com.lincentpega.personalcrmjava.controller.person.dto.response.FullPersonResponse;
import com.lincentpega.personalcrmjava.controller.person.dto.response.ShortPersonResponse;
import com.lincentpega.personalcrmjava.domain.person.Person;
import com.lincentpega.personalcrmjava.exception.ValidationException;
import com.lincentpega.personalcrmjava.service.person.CreatePersonCommand;
import com.lincentpega.personalcrmjava.service.person.PersonService;
import com.lincentpega.personalcrmjava.service.person.UpdatePersonCommand;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final PersonResponsePresentor responsePresentor;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PersonController(PersonService personService, PersonResponsePresentor responsePresentor) {
        this.personService = personService;
        this.responsePresentor = responsePresentor;
    }

    @GetMapping
    public ResponseEntity<PagedResponseWithTotalCount<ShortPersonResponse>> getPersons(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<Person> pagedPersons = personService.getPersons(pageNumber, pageSize);
        List<ShortPersonResponse> persons = pagedPersons.getContent().stream()
                .map(responsePresentor::presentShort)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new PagedResponseWithTotalCount<>(persons, pagedPersons.getTotalElements(), pageNumber, pageSize)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortPersonResponse> getPersonById(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(responsePresentor.presentShort(person));
    }

    @PostMapping
    public ResponseEntity<ShortPersonResponse> createPerson(@RequestBody @Validated CreatePersonRequest request) {
        LocalDate birthDate = null;
        if (request.birthDate() != null) {
            birthDate = parseDate(request.birthDate());
        }

        Person createdPerson = personService.createPerson(
                new CreatePersonCommand(
                        request.firstName(),
                        request.middleName(),
                        request.lastName(),
                        request.gender(),
                        birthDate
                )
        );
        return ResponseEntity.ok(responsePresentor.presentShort(createdPerson));
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
                        birthDate
                )
        );

        return ResponseEntity.ok(responsePresentor.presentFull(updatedPerson));
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
