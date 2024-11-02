package com.lincentpega.personalcrmjava.service.person;

import com.lincentpega.personalcrmjava.data.PersonRepository;
import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.domain.person.ContactInfo;
import com.lincentpega.personalcrmjava.domain.person.JobInfo;
import com.lincentpega.personalcrmjava.domain.person.Person;
import com.lincentpega.personalcrmjava.exception.ApplicationException;
import com.lincentpega.personalcrmjava.service.person.command.CreatePersonCommand;
import com.lincentpega.personalcrmjava.service.person.command.UpdatePersonCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Page<Person> getPersonsWithOwner(long accountId, int pageNumber, int pageSize) {
        return personRepository.findByAccountId(accountId, Pageable.ofSize(pageSize).withPage(pageNumber));
    }

    public Person getPersonByIdWithOwner(long personId, long accountId) {
        return personRepository.findByIdAndAccountId(personId, accountId).orElse(null);
    }

    public Person createPerson(CreatePersonCommand command) {
        Person person = new Person(
                command.firstName(),
                command.lastName(),
                command.middleName(),
                command.gender(),
                command.birthDate(),
                new Account(command.accountId())
        );
        var contactInfosMapped = command.contactInfos().stream()
                .map(dto -> new ContactInfo(dto.method(), dto.data(), person))
                .toList();
        person.getContactInfos().addAll(contactInfosMapped);

        var jobInfosMapped = command.jobInfos().stream()
                .map(dto -> new JobInfo(dto.company(), dto.position(), dto.isCurrent(), person))
                .toList();
        person.getJobInfos().addAll(jobInfosMapped);

        return personRepository.save(person);
    }

    public Person updatePerson(UpdatePersonCommand command) {
        Person person = personRepository.findById(command.id())
                .orElseThrow(() -> new ApplicationException("Person not found"));
        person.setFirstName(command.firstName());
        person.setLastName(command.lastName());
        person.setMiddleName(command.middleName());
        person.setBirthDate(command.birthDate());

        var jobInfos = person.getJobInfos();
        jobInfos.clear();
        var contactInfos = person.getContactInfos();
        contactInfos.clear();

        List<JobInfo> jobInfosMapped;
        if (command.jobInfos() != null) jobInfosMapped = command.jobInfos().stream()
                .map(dto -> new JobInfo(dto.id(), dto.company(), dto.position(), dto.isCurrent(), person))
                .toList();
        else jobInfosMapped = List.of();
        jobInfos.addAll(jobInfosMapped);

        List<ContactInfo> contactInfosMapped;
        if (command.contactInfos() != null) contactInfosMapped = command.contactInfos().stream()
                .map(dto -> new ContactInfo(dto.method(), dto.data(), person))
                .toList();
        else contactInfosMapped = List.of();
        contactInfos.addAll(contactInfosMapped);

        return personRepository.save(person);
    }
}
