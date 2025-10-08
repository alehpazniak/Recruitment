package pl.oleg.recruitment.domain.event;

import pl.oleg.recruitment.domain.RecruitmentStageType;

import java.time.LocalDateTime;

public record StageRejectedEvent(
    Long stageId,
    Long processId,
    RecruitmentStageType stageType,
    String reason,
    LocalDateTime timestamp
) {
}
