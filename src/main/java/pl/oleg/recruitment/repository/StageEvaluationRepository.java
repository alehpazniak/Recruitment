package pl.oleg.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.oleg.recruitment.domain.StageEvaluation;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageEvaluationRepository extends JpaRepository<StageEvaluation, Long> {

    List<StageEvaluation> findByStageId(Long stageId);

    Optional<StageEvaluation> findByStageIdAndEvaluatorId(Long stageId, Long evaluatorId);

    @Query("SELECT COUNT(e) FROM StageEvaluation e " +
            "WHERE e.stage.id = :stageId AND e.approved = false")
    Long countRejectionsByStageId(@Param("stageId") Long stageId);

    @Query("SELECT COUNT(e) FROM StageEvaluation e WHERE e.stage.id = :stageId")
    Long countEvaluationsByStageId(@Param("stageId") Long stageId);
}
