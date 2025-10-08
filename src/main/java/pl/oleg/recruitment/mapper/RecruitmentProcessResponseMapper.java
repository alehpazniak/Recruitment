package pl.oleg.recruitment.mapper;

import org.springframework.stereotype.Component;
import pl.oleg.recruitment.domain.*;
import pl.oleg.recruitment.dto.*;

@Component
public class RecruitmentProcessResponseMapper {

    public RecruitmentProcessResponse mapToRecruitmentProcessResponse(RecruitmentProcess process) {
        return new RecruitmentProcessResponse(
            process.getId(),
            process.getCandidateName(),
            process.getCandidateEmail(),
            process.getPositionTitle(),
            process.getCurrentStatus(),
            process.getCreatedAt(),
            process.getCompletedAt(),
            process.getStages().stream()
                .map(this::mapToStageResponse)
                .toList()
        );
    }

    public RecruitmentStageResponse mapToStageResponse(RecruitmentStage stage) {
        return new RecruitmentStageResponse(
            stage.getId(),
            stage.getStageType(),
            stage.getStageOrder(),
            stage.getStatus(),
            stage.getScheduledAt(),
            stage.getStartedAt(),
            stage.getCompletedAt(),
            stage.getAssignmentDeadline(),
            stage.getAssignmentSubmittedAt(),
            stage.getDeadlineOverrideApproved(),
            stage.getParticipants().stream()
                .map(this::mapToParticipantResponse)
                .toList(),
            stage.getQuestionCards().stream()
                .map(this::mapToQuestionCardResponse)
                .toList(),
            stage.getEvaluations().stream()
                .map(this::mapToEvaluationResponse)
                .toList(),
            stage.getNotes()
        );
    }

    public ParticipantResponse mapToParticipantResponse(StageParticipant participant) {
        return new ParticipantResponse(
            participant.getId(),
            participant.getUserId(),
            participant.getUserEmail(),
            participant.getUserName(),
            participant.getConfirmationStatus(),
            participant.getIsRequired(),
            participant.getConfirmedAt(),
            participant.getConfirmationNote()
        );
    }

    public QuestionCardResponse mapToQuestionCardResponse(QuestionCard card) {
        return new QuestionCardResponse(
            card.getId(),
            card.getQuestion(),
            card.getExpectedAnswer(),
            card.getOrderIndex(),
            card.getIsActive(),
            card.getEvaluationCriteria());
    }

    public EvaluationResponse mapToEvaluationResponse(StageEvaluation evaluation) {
        return new EvaluationResponse(
            evaluation.getId(),
            evaluation.getEvaluatorId(),
            evaluation.getEvaluatorName(),
            evaluation.getApproved(),
            evaluation.getFeedback(),
            evaluation.getEvaluatedAt(),
            evaluation.getQuestionEvaluations().stream()
                .map(this::mapToQuestionEvaluationResponse)
                .toList());
    }

    public QuestionEvaluationResponse mapToQuestionEvaluationResponse(QuestionEvaluation qe) {
        return new QuestionEvaluationResponse(
            qe.getId(),
            qe.getQuestionCard().getId(),
            qe.getQuestionCard().getQuestion(),
            qe.getAnswer(),
            qe.getScore(),
            qe.getComment());
    }
}
