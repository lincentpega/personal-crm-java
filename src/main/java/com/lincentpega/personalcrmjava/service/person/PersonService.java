package com.lincentpega.personalcrmjava.service.person;

import com.lincentpega.personalcrmjava.data.PersonRepository;
import com.lincentpega.personalcrmjava.domain.person.Person;
import com.lincentpega.personalcrmjava.exception.ApplicationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Page<Person> getPersons(int pageNumber, int pageSize) {
        return personRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNumber));
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person createPerson(CreatePersonCommand command) {
        Person person = new Person(
            command.firstName(),
            command.lastName(),
            command.middleName(),
            command.gender(),
            command.birthDate()
        );
        return personRepository.save(person);
    }

    public Person updatePerson(UpdatePersonCommand command) {
        Person person = personRepository.findById(command.id())
            .orElseThrow(() -> new ApplicationException("Person not found"));
        person.setFirstName(command.firstName());
        person.setLastName(command.lastName());
        person.setMiddleName(command.middleName());
        person.setBirthDate(command.birthDate());
        return personRepository.save(person);
    }
}
