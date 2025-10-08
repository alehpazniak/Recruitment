package pl.oleg.recruitment.dto;

import pl.oleg.recruitment.domain.StageStatus;

import java.time.LocalDateTime;
import java.util.List;

public record RecruitmentProcessResponse(
        Long id,
        String candidateName,
        String candidateEmail,
        String positionTitle,
        StageStatus currentStatus,
        LocalDateTime createdAt,
        LocalDateTime completedAt,
        List<RecruitmentStageResponse> stages
) {
}
