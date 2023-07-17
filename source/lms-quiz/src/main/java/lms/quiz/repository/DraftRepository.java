package lms.quiz.repository;

import lms.quiz.domain.DraftEntity;
import lms.quiz.enums.DraftContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DraftRepository extends JpaRepository<DraftEntity, Long> {

    Optional<DraftEntity> findDraftEntityByContextAndContextId(DraftContext context, Long contextId);

    boolean existsByContextAndContextId(DraftContext context, Long contextId);
}
