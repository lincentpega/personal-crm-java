package com.lincentpega.personalcrmjava.domain.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Entity
@Table(name = "job_info", indexes = {
        @Index(name = "idx_job_info_person_id", columnList = "person_id")
})
public class JobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "position")
    private @Nullable String position;

    @Column(name = "is_current", nullable = false)
    private boolean isCurrent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
