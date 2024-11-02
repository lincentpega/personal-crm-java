package com.lincentpega.personalcrmjava.domain.person;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact_info", indexes = {
        @Index(name = "idx_contact_info_person_id", columnList = "person_id")
})
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "data", nullable = false)
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public ContactInfo(String method, String data, Person person) {
        this.method = method;
        this.data = data;
        this.person = person;
    }
}
