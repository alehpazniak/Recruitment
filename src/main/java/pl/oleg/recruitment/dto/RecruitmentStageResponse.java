package pl.oleg.recruitment.dto;

import pl.oleg.recruitment.domain.RecruitmentStageType;
import pl.oleg.recruitment.domain.StageStatus;

import java.time.LocalDateTime;
import java.util.List;

public record RecruitmentStageResponse(
        Long id,
        RecruitmentStageType stageType,
        Integer stageOrder,
        StageStatus status,
        LocalDateTime scheduledAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        LocalDateTime assignmentDeadline,
        LocalDateTime assignmentSubmittedAt,
        Boolean deadlineOverrideApproved,
        List<ParticipantResponse> participants,
        List<QuestionCardResponse> questionCards,
        List<EvaluationResponse> evaluations,
        String notes
) {
}
