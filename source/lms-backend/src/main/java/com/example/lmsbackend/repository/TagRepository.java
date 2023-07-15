package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.notification.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

    List<TagEntity> findTagEntitiesByNameIn(List<String> names);
}
