package pl.oleg.recruitment.domain.event;

import java.time.LocalDateTime;

public record ParticipationConfirmedEvent(
    Long participantId,
    Long stageId,
    Long userId,
    Boolean confirmed,
    LocalDateTime timestamp
) {
}
