package pl.oleg.recruitment.domain.event;

import pl.oleg.recruitment.domain.RecruitmentStageType;

import java.time.LocalDateTime;
import java.util.List;

public record ConfirmationsRequestedEvent(
    Long stageId,
    Long processId,
    RecruitmentStageType stageType,
    List<Long> participantUserIds,
    LocalDateTime timestamp
) {
}
