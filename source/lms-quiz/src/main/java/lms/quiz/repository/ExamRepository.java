package lms.quiz.repository;

import lms.quiz.domain.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

//    @Query("select e from ExamEntity e left join fetch e.questions where e.id = (?1)")
//    Optional<ExamEntity> findFetchQuestionsById(Long id);
}
