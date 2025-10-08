package pl.oleg.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.oleg.recruitment.domain.RecruitmentStage;
import pl.oleg.recruitment.domain.StageStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentStageRepository extends JpaRepository<RecruitmentStage, Long> {

    List<RecruitmentStage> findByRecruitmentProcessIdOrderByStageOrderAsc(Long processId);

    @Query("SELECT s FROM RecruitmentStage s " +
            "JOIN FETCH s.participants " +
            "WHERE s.id = :id")
    Optional<RecruitmentStage> findByIdWithParticipants(@Param("id") Long id);

    @Query("SELECT s FROM RecruitmentStage s " +
            "JOIN FETCH s.questionCards " +
            "WHERE s.id = :id")
    Optional<RecruitmentStage> findByIdWithQuestionCards(@Param("id") Long id);

    @Query("SELECT s FROM RecruitmentStage s " +
            "WHERE s.status = :status " +
            "AND s.assignmentDeadline < :now " +
            "AND s.stageType = 'ASSIGNMENT'")
    List<RecruitmentStage> findOverdueAssignments(
            @Param("status") StageStatus status,
            @Param("now") LocalDateTime now
    );
}
