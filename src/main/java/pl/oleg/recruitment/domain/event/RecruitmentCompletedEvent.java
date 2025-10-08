package pl.oleg.recruitment.domain.event;

import java.time.LocalDateTime;

public record RecruitmentCompletedEvent(
    Long processId,
    String candidateName,
    String candidateEmail,
    LocalDateTime timestamp
) {
}
