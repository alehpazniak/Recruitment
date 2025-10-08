package pl.oleg.recruitment.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EvaluationResponse(
        Long id,
        Long evaluatorId,
        String evaluatorName,
        Boolean approved,
        String feedback,
        LocalDateTime evaluatedAt,
        List<QuestionEvaluationResponse> questionEvaluations
) {
}
