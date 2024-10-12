package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "SELECT * FROM people p " +
            "WHERE DATE_PART('MONTH', p.birth_date) = DATE_PART('MONTH', CURRENT_TIMESTAMP AT TIME ZONE :timezone) " +
            "AND DATE_PART('DAY', p.birth_date) = DATE_PART('DAY', CURRENT_TIMESTAMP AT TIME ZONE :timezone)",
            nativeQuery = true)
    Iterable<Person> getPeopleWithBirthday(@Param("timezone") String timezone);
}
