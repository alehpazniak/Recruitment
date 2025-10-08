package pl.oleg.recruitment.domain.event;

import java.time.LocalDateTime;

public record AssignmentSubmittedEvent(
    Long stageId,
    Long processId,
    LocalDateTime submittedAt,
    LocalDateTime deadline,
    Boolean isLate,
    LocalDateTime timestamp
) {
}
