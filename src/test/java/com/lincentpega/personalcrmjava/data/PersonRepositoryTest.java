package com.lincentpega.personalcrmjava.data;

import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.domain.person.Person;
import com.lincentpega.personalcrmjava.domain.person.PersonGender;
import com.lincentpega.personalcrmjava.domain.person.PersonSettings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest {

    public static final long ACCOUNT_ID = 1L;
    public static final String ACCOUNT_LOCALE = "ru";
    public static final String ACCOUNT_EMAIL = "example@gmail.com";
    public static final String ACCOUNT_USERNAME = "test_username";
    public static final String ACCOUNT_PASSWORD_HASH = "password_hash";

    public static final long PERSON1_ID = 1L;
    public static final long PERSON2_ID = 2L;
    public static final long PERSON3_ID = 3L;
    public static final String PERSON_FIRST_NAME = "John";
    public static final String PERSON_MIDDLE_NAME = "Williams";
    public static final String PERSON_LAST_NAME = "Conor";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void getPeopleWithBirthday_shouldReturnAllPeopleWithBirthday() {
        var account = new Account(ACCOUNT_ID, ACCOUNT_LOCALE, ACCOUNT_EMAIL, ACCOUNT_USERNAME, ACCOUNT_PASSWORD_HASH, Instant.now(), Instant.now(), Set.of());
        accountRepository.save(account);

        var person1Birthday = LocalDate.of(1997, Month.DECEMBER, 19);
        var person1 = new Person(PERSON1_ID, PERSON_FIRST_NAME, PERSON_MIDDLE_NAME, PERSON_LAST_NAME, PersonGender.MALE, new PersonSettings(1, true), person1Birthday, false, account);
        personRepository.save(person1);

        var person2Birthday = LocalDate.of(1997, Month.DECEMBER, 20);
        var person2 = new Person(PERSON2_ID, PERSON_FIRST_NAME, PERSON_MIDDLE_NAME, PERSON_LAST_NAME, PersonGender.MALE, new PersonSettings(2, true), person2Birthday, false, account);
        personRepository.save(person2);

        var person3Birthday = LocalDate.of(1997, Month.DECEMBER, 20);
        var person3 = new Person(PERSON3_ID, PERSON_FIRST_NAME, PERSON_MIDDLE_NAME, PERSON_LAST_NAME, PersonGender.MALE, new PersonSettings(3, true), person3Birthday, false, account);
        personRepository.save(person3);

        var zoneId = ZoneId.of("Europe/Moscow");
        var expectedDate = LocalDate.of(1997, Month.DECEMBER, 20).atStartOfDay(zoneId).toLocalDate();
        var people = personRepository.getPeopleWithBirthday(account.getId(), expectedDate);
        assertThat(people)
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(person2, person3);
    }
}