package com.lincentpega.personalcrmjava.domain.person;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id = 0;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private @Nullable PersonGender gender;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "person",
            cascade = {CascadeType.ALL}
    )
    private final List<ContactInfo> contactInfos = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "person",
            cascade = {CascadeType.ALL}
    )
    private final List<JobInfo> jobInfos = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "settings_id")
    private PersonSettings settings;

    @Column(name = "birth_date")
    private @Nullable LocalDate birthDate;


    public Person(@NonNull String firstName,
                  @Nullable String middleName,
                  @Nullable String lastName,
                  @Nullable PersonGender gender,
                  @Nullable LocalDate birthDate) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.settings = new PersonSettings();
    }
}
