package pl.oleg.recruitment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record QuestionCardDTO(
        @NotBlank String question,
        String expectedAnswer,
        @NotNull Integer orderIndex,
        String evaluationCriteria
) {
}
