package com.lincentpega.personalcrmjava.controller.person;

import com.lincentpega.personalcrmjava.configuration.security.SecurityConfiguration;
import com.lincentpega.personalcrmjava.domain.account.Account;
import com.lincentpega.personalcrmjava.domain.account.AccountRole;
import com.lincentpega.personalcrmjava.domain.person.*;
import com.lincentpega.personalcrmjava.service.account.AccountService;
import com.lincentpega.personalcrmjava.service.person.command.UpdatePersonCommand;
import com.lincentpega.personalcrmjava.service.person.dto.create.CreateContactInfoDto;
import com.lincentpega.personalcrmjava.service.person.command.CreatePersonCommand;
import com.lincentpega.personalcrmjava.service.person.dto.create.CreateJobInfoDto;
import com.lincentpega.personalcrmjava.service.person.PersonService;
import com.lincentpega.personalcrmjava.service.person.dto.update.UpdateContactInfoDto;
import com.lincentpega.personalcrmjava.service.person.dto.update.UpdateJobInfoDto;
import com.lincentpega.personalcrmjava.service.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ActiveProfiles("test")
@WebMvcTest(controllers = PersonController.class)
@Import(SecurityConfiguration.class)
class PersonControllerTest {

    public static final long PERSON1_ID = 111L;
    public static final String PERSON1_FIRST_NAME = "John";
    public static final String PERSON1_MIDDLE_NAME = "Williams";
    public static final String PERSON1_LAST_NAME = "Conor";
    public static final LocalDate PERSON1_BIRTH_DATE = LocalDate.of(1997, 12, 13);

    public static final long PERSON2_ID = 222L;
    public static final String PERSON2_FIRST_NAME = "Sarah";
    public static final String PERSON2_LAST_NAME = "Conor";

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 10;

    public static final String IDEMPOTENCY_KEY = "36a6bfc6-d4f0-4dea-a736-3961ae09f28f";

    public static final long JOB_INFO1_ID = 333L;
    public static final long JOB_INFO2_ID = 444L;
    public static final long CONTACT_INFO1_ID = 555L;
    public static final long CONTACT_INFO2_ID = 666L;
    public static final String COMPANY1 = "SberBank";
    public static final String COMPANY2 = "McKinsey";
    public static final String JOB_POSITION1 = "Software Engineer";
    public static final String JOB_POSITION2 = "Junior Software Developer";

    public static final String CONTACT_METHOD1 = "telegram";
    public static final String CONTACT_METHOD2 = "phone";
    public static final String CONTACT_DATA1 = "telegram_nickname";
    public static final String CONTACT_DATA2 = "+78005555535";

    public static final String ACCOUNT_USERNAME = "john";
    public static final long ACCOUNT_ID = 777L;

    @Value("classpath:testdata/people/response/get-people-success.json")
    private Resource getPeopleSuccessResponse;
    @Value("classpath:testdata/people/response/get-person-by-id-success.json")
    private Resource getPersonByIdSuccessResponse;
    @Value("classpath:testdata/people/request/create-person.json")
    private Resource createPersonRequest;
    @Value("classpath:testdata/people/request/update-person.json")
    private Resource updatePersonRequest;


    @MockBean
    private PersonService personService;
    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;

    private String authToken;

    @BeforeEach
    void setUp() {
        var account = new Account();
        account.setId(ACCOUNT_ID);
        account.setUsername(ACCOUNT_USERNAME);
        authToken = jwtService.generateAccessToken(account);

        var role = new AccountRole();
        role.setName("user");

        account.setRoles(Set.of(role));

        when(accountService.getAccountByUsername(ACCOUNT_USERNAME)).thenReturn(Optional.of(account));
    }

    @Test
    public void getPeople_shouldReturnOk() throws Exception {
        // Arrange
        var people = List.of(getPerson1(), getPerson2());
        var peoplePage = new PageImpl<>(people, Pageable.ofSize(PAGE_SIZE).withPage(PAGE_NUMBER), people.size());
        when(personService.getPersonsWithOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(peoplePage);

        // Act
        var request = get("/api/people")
                .header("Authorization", "Bearer " + authToken);

        var result = this.mockMvc.perform(request).andDo(print());

        // Assert
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getPeopleSuccessResponse.getContentAsString(StandardCharsets.UTF_8)));

        verify(personService).getPersonsWithOwner(ACCOUNT_ID, PAGE_NUMBER, PAGE_SIZE);
    }

    @Test
    public void getPeopleById_shouldReturnOk() throws Exception {
        // Arrange
        when(personService.getPersonByIdWithOwner(anyLong(), anyLong()))
                .thenReturn(getPerson1());

        // Act
        var request = get("/api/people/" + PERSON1_ID)
                .header("Authorization", "Bearer " + authToken);

        var result = this.mockMvc.perform(request).andDo(print());

        // Assert
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getPersonByIdSuccessResponse.getContentAsString(StandardCharsets.UTF_8)));

        verify(personService).getPersonByIdWithOwner(PERSON1_ID, ACCOUNT_ID);
    }

    @Test
    public void createPerson_shouldCallPersonService() throws Exception {
        // Arrange
        when(personService.createPerson(any(CreatePersonCommand.class)))
                .thenReturn(getPerson1());

        // Act
        var request = post("/api/people")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Idempotency-Key", IDEMPOTENCY_KEY)
                .header("Authorization", "Bearer " + authToken)
                .content(createPersonRequest.getContentAsString(StandardCharsets.UTF_8));

        var result = mockMvc.perform(request).andDo(print());

        // Assert
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getPersonByIdSuccessResponse.getContentAsString(StandardCharsets.UTF_8)));

        verify(personService).createPerson(
                eq(
                        new CreatePersonCommand(
                                ACCOUNT_ID,
                                PERSON1_FIRST_NAME,
                                PERSON1_MIDDLE_NAME,
                                PERSON1_LAST_NAME,
                                PersonGender.MALE,
                                PERSON1_BIRTH_DATE,
                                Set.of(new CreateContactInfoDto(CONTACT_METHOD1, CONTACT_DATA1), new CreateContactInfoDto(CONTACT_METHOD2, CONTACT_DATA2)),
                                Set.of(new CreateJobInfoDto(COMPANY1, JOB_POSITION1, true), new CreateJobInfoDto(COMPANY2, JOB_POSITION2, false))
                        )
                )
        );
    }

    @Test
    public void updatePerson_shouldCallPersonService() throws Exception {
        // Arrange
        when(personService.updatePerson(any(UpdatePersonCommand.class)))
                .thenReturn(getPerson1());

        // Act
        var request = put("/api/people/" + PERSON1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authToken)
                .content(updatePersonRequest.getContentAsString(StandardCharsets.UTF_8));

        var result = mockMvc.perform(request).andDo(print());

        // Assert
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getPersonByIdSuccessResponse.getContentAsString(StandardCharsets.UTF_8)));

        verify(personService).updatePerson(
                eq(
                        new UpdatePersonCommand(
                                PERSON1_ID,
                                PERSON1_FIRST_NAME,
                                PERSON1_MIDDLE_NAME,
                                PERSON1_LAST_NAME,
                                PERSON1_BIRTH_DATE,
                                Set.of(new UpdateContactInfoDto(CONTACT_INFO1_ID, CONTACT_METHOD1, CONTACT_DATA1), new UpdateContactInfoDto(CONTACT_INFO2_ID, CONTACT_METHOD2, CONTACT_DATA2)),
                                Set.of(new UpdateJobInfoDto(JOB_INFO1_ID, COMPANY1, JOB_POSITION1, true), new UpdateJobInfoDto(JOB_INFO2_ID, COMPANY2, JOB_POSITION2, false))
                        )
                )
        );
    }

    private static Person getPerson1() {
        var person = new Person(
                PERSON1_ID,
                PERSON1_FIRST_NAME,
                PERSON1_MIDDLE_NAME,
                PERSON1_LAST_NAME,
                PersonGender.MALE,
                new PersonSettings(),
                PERSON1_BIRTH_DATE,
                null
        );

        var jobInfo1 = getJobInfo1(person);
        var jobInfo2 = getJobInfo2(person);
        person.getJobInfos().add(jobInfo1);
        person.getJobInfos().add(jobInfo2);

        var contactInfo1 = getContactInfo1(person);
        var contactInfo2 = getContactInfo2(person);
        person.getContactInfos().add(contactInfo1);
        person.getContactInfos().add(contactInfo2);

        return person;
    }

    private static ContactInfo getContactInfo2(Person person) {
        return new ContactInfo(CONTACT_INFO2_ID, CONTACT_METHOD2, CONTACT_DATA2, person);
    }

    private static ContactInfo getContactInfo1(Person person) {
        return new ContactInfo(CONTACT_INFO1_ID, CONTACT_METHOD1, CONTACT_DATA1, person);
    }

    private static JobInfo getJobInfo2(Person person) {
        return new JobInfo(JOB_INFO2_ID, COMPANY2, JOB_POSITION2, false, person);
    }

    private static JobInfo getJobInfo1(Person person) {
        return new JobInfo(JOB_INFO1_ID, COMPANY1, JOB_POSITION1, true, person);
    }

    private static Person getPerson2() {
        return new Person(
                PERSON2_ID,
                PERSON2_FIRST_NAME,
                null,
                PERSON2_LAST_NAME,
                null,
                null,
                null,
                null
        );
    }
}