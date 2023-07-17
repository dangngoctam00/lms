package lms.quiz.service;

import exception.ResourceNotFound;
import lms.quiz.domain.ExamEntity;
import lms.quiz.domain.QuizQuestionEntity;
import lms.quiz.enums.QuizContext;
import lms.quiz.repository.QuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.enhanced.SequenceIdRevisionEntity;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizQuestionService {

    private final QuizQuestionRepository questionRepository;
    private final ExamService examService;

    @PersistenceContext
    private final EntityManager entityManager;

    public void createQuestion(QuizQuestionEntity data) {
        if (data.getContext() == QuizContext.EXAM) {
            var exam = examService.getById(data.getContextId());
            exam.setUpdatedAt(LocalDateTime.now());
        }
        questionRepository.save(data);
    }

    public QuizQuestionEntity getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(ResourceNotFound::new);
    }

    public List<QuizQuestionEntity> getQuestionsByExamAndRevision(Long id, Integer revision) {
        AuditQuery q = AuditReaderFactory.get(entityManager)
                .createQuery()
                .forEntitiesAtRevision(QuizQuestionEntity.class, revision);
        q.add(AuditEntity.property("context").eq(QuizContext.EXAM));
        q.add(AuditEntity.property("contextId").eq(id));

        List<?> questions = q.getResultList();
        return questions.stream()
                .map(QuizQuestionEntity.class::cast)
                .collect(Collectors.toList());
    }

    public List<QuizQuestionEntity> getQuestionsByExam(Long id) {
        AuditQuery auditQuery = AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntity(QuizQuestionEntity.class, false, false);
        auditQuery.addProjection(AuditEntity.revisionNumber().max());
        auditQuery.add(AuditEntity.property("context").eq(QuizContext.EXAM));
        auditQuery.add(AuditEntity.property("contextId").eq(id));
        Number maxRevision = (Number) auditQuery.getSingleResult();

        if (maxRevision != null) {
            AuditQuery q = AuditReaderFactory.get(entityManager)
                    .createQuery()
                    .forEntitiesAtRevision(QuizQuestionEntity.class, maxRevision);
            auditQuery.add(AuditEntity.property("context").eq(QuizContext.EXAM));
            auditQuery.add(AuditEntity.property("contextId").eq(id));

            List<?> questions = q.getResultList();
            return questions.stream()
                    .map(QuizQuestionEntity.class::cast)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
