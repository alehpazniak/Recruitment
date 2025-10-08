package pl.oleg.recruitment.dto;

import javax.validation.constraints.NotNull;

public record QuestionEvaluationDTO(
        @NotNull Long questionCardId,
        String answer,
        Integer score,
        String comment
) {
}
