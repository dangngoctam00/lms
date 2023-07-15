package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.UnitClassTextBookEntity;
import com.example.lmsbackend.domain.compositekey.UnitTextBookKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitClassTextbookRepository extends JpaRepository<UnitClassTextBookEntity, UnitTextBookKey> {
}
