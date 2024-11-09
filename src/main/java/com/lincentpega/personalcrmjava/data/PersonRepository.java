package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.person.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "SELECT * FROM people p " +
            "WHERE DATE_PART('MONTH', p.birth_date) = DATE_PART('MONTH', CAST(:date AS DATE)) " +
            "AND DATE_PART('DAY', p.birth_date) = DATE_PART('DAY', CAST(:date AS DATE)) " +
            "AND p.account_id = :accountId " +
            "AND NOT p.is_deleted",
            nativeQuery = true)
    Iterable<Person> getPeopleWithBirthday(long accountId, @Param("date") LocalDate date);

    @Query("select p from Person p where p.id = :id and p.account.id = :accountId and p.isDeleted = false")
    Optional<Person> findById(Long id, long accountId);

    @Query("select p from Person p where p.account.id = :accountId and p.isDeleted = false order by p.id")
    Page<Person> findAll(long accountId, Pageable pageable);
}
