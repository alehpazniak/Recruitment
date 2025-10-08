package pl.oleg.recruitment.dto;

import javax.validation.constraints.NotNull;

public record ApproveDeadlineOverrideRequest(
        @NotNull Long stageId,
        @NotNull Boolean approved,
        String reason
) {
}
