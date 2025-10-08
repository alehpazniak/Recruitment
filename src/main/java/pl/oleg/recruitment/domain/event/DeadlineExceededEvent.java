package pl.oleg.recruitment.domain.event;

import java.time.LocalDateTime;

public record DeadlineExceededEvent(
    Long stageId,
    Long processId,
    LocalDateTime deadline,
    LocalDateTime submittedAt,
    LocalDateTime timestamp
) {
}
