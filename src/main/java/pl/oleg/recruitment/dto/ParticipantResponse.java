package pl.oleg.recruitment.dto;

import pl.oleg.recruitment.domain.ParticipantConfirmationStatus;

import java.time.LocalDateTime;

public record ParticipantResponse(
        Long id,
        Long userId,
        String userEmail,
        String userName,
        ParticipantConfirmationStatus confirmationStatus,
        Boolean isRequired,
        LocalDateTime confirmedAt,
        String confirmationNote
) {
}
