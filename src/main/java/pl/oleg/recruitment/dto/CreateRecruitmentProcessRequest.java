package pl.oleg.recruitment.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public record CreateRecruitmentProcessRequest(
        @NotBlank String candidateName,
        @NotBlank @Email String candidateEmail,
        @NotBlank String positionTitle,
        @NotNull List<StageDefinitionDTO> stageDefinitions) {
}
