package com.example.lmsbackend.domain.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Table(name = "calendar")
@Entity
@FieldNameConstants
@Getter
@Setter
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CalendarType type;

    @Column(name = "type_id")
    private Long typeId;

    @OneToMany(fetch = LAZY, cascade = {CascadeType.REMOVE}, mappedBy = "calendar")
    private List<EventEntity> events = new ArrayList<>();
}
