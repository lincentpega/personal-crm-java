package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
