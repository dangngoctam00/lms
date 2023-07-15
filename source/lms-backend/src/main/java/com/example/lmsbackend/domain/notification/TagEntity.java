package com.example.lmsbackend.domain.notification;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "tag")
@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"notifications"} )
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<AnnouncementEntity> notifications = new HashSet<>();
}
