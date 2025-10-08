package pl.oleg.recruitment.domain.event;

import pl.oleg.recruitment.domain.RecruitmentStageType;

import java.time.LocalDateTime;

public record NextStageReadyEvent(
    Long processId,
    Long nextStageId,
    RecruitmentStageType nextStageType,
    LocalDateTime timestamp
) {
}
