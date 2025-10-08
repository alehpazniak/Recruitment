package pl.oleg.recruitment.dto;

import pl.oleg.recruitment.domain.RecruitmentStageType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record StageDefinitionDTO(
        @NotNull RecruitmentStageType stageType,
        @NotNull Integer stageOrder,
        List<Long> participantUserIds,
        List<QuestionCardDTO> questionCards,
        LocalDateTime scheduledAt,
        LocalDateTime assignmentDeadline) {
}
