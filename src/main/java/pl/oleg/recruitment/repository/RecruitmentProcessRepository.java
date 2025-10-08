package pl.oleg.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.oleg.recruitment.domain.RecruitmentProcess;
import pl.oleg.recruitment.domain.StageStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, Long> {

    List<RecruitmentProcess> findByCurrentStatus(StageStatus status);

    List<RecruitmentProcess> findByCandidateEmail(String email);

    @Query("SELECT rp FROM RecruitmentProcess rp " +
            "JOIN FETCH rp.stages s " +
            "WHERE rp.id = :id")
    Optional<RecruitmentProcess> findByIdWithStages(@Param("id") Long id);
}
