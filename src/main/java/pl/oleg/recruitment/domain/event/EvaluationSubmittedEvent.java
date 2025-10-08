package pl.oleg.recruitment.domain.event;

import java.time.LocalDateTime;

public record EvaluationSubmittedEvent(
    Long evaluationId,
    Long stageId,
    Long evaluatorId,
    Boolean approved,
    LocalDateTime timestamp
) {
}
