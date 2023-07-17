package lms.quiz.service;

import exception.ResourceNotFound;
import lms.quiz.domain.DraftEntity;
import lms.quiz.domain.ExamEntity;
import lms.quiz.domain.QuizQuestionEntity;
import lms.quiz.enums.DraftContext;
import lms.quiz.enums.ExamContext;
import lms.quiz.enums.QuizContext;
import lms.quiz.exception.DraftAlreadyExistException;
import lms.quiz.repository.DraftRepository;
import lms.quiz.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final EntityManager entityManager;
    private final DraftRepository draftRepository;

    public void createExam(ExamEntity entity) {
        examRepository.save(entity);
    }

    public ExamEntity getById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    public Optional<ExamEntity> getByIdAndRevision(Long id, Integer revision) {
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager)
                .createQuery()
                .forEntitiesAtRevision(ExamEntity.class, revision);
        auditQuery.add(AuditEntity.property("id").eq(id));
        return Optional.ofNullable((ExamEntity)auditQuery.getSingleResult());
    }

    public void createDraft(Long examId) {
        if (draftRepository.existsByContextAndContextId(DraftContext.EXAM, examId)) {
            throw new DraftAlreadyExistException(examId);
        }
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntity(ExamEntity.class, false, false);
        auditQuery.addProjection(AuditEntity.revisionNumber().max());
        auditQuery.add(AuditEntity.property("id").eq(examId));
        Number maxRevision = (Number) auditQuery.getSingleResult();
        var draft = new DraftEntity();
        draft.setContext(DraftContext.EXAM);
        draft.setContextId(examId);
        draft.setCreatedAt(LocalDateTime.now());
        draft.setUpdatedAt(LocalDateTime.now());
        draft.setRevisionNumber(Optional.ofNullable(maxRevision).orElse(0L).intValue() + 1);
        draftRepository.save(draft);
    }

}
