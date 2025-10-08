package pl.oleg.recruitment.domain.event;

import java.time.LocalDateTime;

public record RecruitmentCreatedEvent(
    Long processId,
    String candidateName,
    String candidateEmail,
    String positionTitle,
    LocalDateTime timestamp
) {
}
