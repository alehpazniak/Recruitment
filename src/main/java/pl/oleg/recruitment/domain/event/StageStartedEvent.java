package pl.oleg.recruitment.domain.event;

import pl.oleg.recruitment.domain.RecruitmentStageType;

import java.time.LocalDateTime;

public record StageStartedEvent(
    Long stageId,
    Long processId,
    RecruitmentStageType stageType,
    LocalDateTime timestamp
) {
}
