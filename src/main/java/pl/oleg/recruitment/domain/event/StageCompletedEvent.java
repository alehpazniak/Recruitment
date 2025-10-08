package pl.oleg.recruitment.domain.event;

import pl.oleg.recruitment.domain.RecruitmentStageType;

import java.time.LocalDateTime;

public record StageCompletedEvent(
    Long stageId,
    Long processId,
    RecruitmentStageType stageType,
    LocalDateTime timestamp
) {
}
