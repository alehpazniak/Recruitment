package pl.oleg.recruitment.dto;

public record QuestionEvaluationResponse(
        Long id,
        Long questionCardId,
        String question,
        String answer,
        Integer score,
        String comment
) {
}
