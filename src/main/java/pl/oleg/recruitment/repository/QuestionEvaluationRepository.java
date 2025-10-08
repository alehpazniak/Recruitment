package pl.oleg.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.oleg.recruitment.domain.QuestionEvaluation;

import java.util.List;

@Repository
public interface QuestionEvaluationRepository extends JpaRepository<QuestionEvaluation, Long> {

    List<QuestionEvaluation> findByEvaluationId(Long evaluationId);
}
