package pl.oleg.recruitment.dto;

public record QuestionCardResponse(
        Long id,
        String question,
        String expectedAnswer,
        Integer orderIndex,
        Boolean isActive,
        String evaluationCriteria
) {
}
