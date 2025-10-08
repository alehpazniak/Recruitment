package pl.oleg.recruitment.dto;

import javax.validation.constraints.NotNull;

public record ConfirmParticipationRequest(
        @NotNull Long stageId,
        @NotNull Long userId,
        @NotNull Boolean confirmed,
        String note
) {
}
