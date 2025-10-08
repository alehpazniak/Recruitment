package pl.oleg.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.oleg.recruitment.domain.StageParticipant;

import java.util.List;
import java.util.Optional;

@Repository
public interface StageParticipantRepository extends JpaRepository<StageParticipant, Long> {

    List<StageParticipant> findByStageId(Long stageId);

    Optional<StageParticipant> findByStageIdAndUserId(Long stageId, Long userId);

    @Query("SELECT sp FROM StageParticipant sp " +
            "WHERE sp.stage.id = :stageId " +
            "AND sp.isRequired = true " +
            "AND sp.confirmationStatus != 'CONFIRMED'")
    List<StageParticipant> findUnconfirmedRequiredParticipants(@Param("stageId") Long stageId);
}
