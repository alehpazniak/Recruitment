package pl.oleg.recruitment.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.oleg.recruitment.domain.QuestionCard;

import java.util.List;

@Repository
public interface QuestionCardRepository extends JpaRepository<QuestionCard, Long> {

    List<QuestionCard> findByStageIdAndIsActiveTrueOrderByOrderIndexAsc(Long stageId);

    @Query("SELECT COUNT(qc) FROM QuestionCard qc " +
            "WHERE qc.stage.id = :stageId AND qc.isActive = true")
    Long countActiveQuestionsByStageId(@Param("stageId") Long stageId);
}
