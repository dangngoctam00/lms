package lms.quiz.repository;

import lms.quiz.domain.QuizQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestionEntity, Long> {

}
