package pl.oleg.recruitment.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record SubmitEvaluationRequest(
        @NotNull Long stageId,
        @NotNull Long evaluatorId,
        @NotNull Boolean approved,
        String feedback,
        @NotNull List<QuestionEvaluationDTO> questionEvaluations
) {
}
