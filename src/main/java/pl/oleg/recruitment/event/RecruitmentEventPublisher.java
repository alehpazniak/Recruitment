package pl.oleg.recruitment.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.oleg.recruitment.domain.*;
import pl.oleg.recruitment.domain.event.*;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RecruitmentEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public RecruitmentEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishRecruitmentCreated(RecruitmentProcess process) {
        RecruitmentCreatedEvent event = new RecruitmentCreatedEvent(
            process.getId(),
            process.getCandidateName(),
            process.getCandidateEmail(),
            process.getPositionTitle(),
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishStageStarted(RecruitmentStage stage) {
        StageStartedEvent event = new StageStartedEvent(
            stage.getId(),
            stage.getRecruitmentProcess().getId(),
            stage.getStageType(),
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishConfirmationsRequested(RecruitmentStage stage) {
        List<Long> participantIds = stage.getParticipants().stream()
            .map(StageParticipant::getUserId)
            .toList();

        ConfirmationsRequestedEvent event = new ConfirmationsRequestedEvent(
            stage.getId(),
            stage.getRecruitmentProcess().getId(),
            stage.getStageType(),
            participantIds,
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishParticipationConfirmed(StageParticipant participant) {
        ParticipationConfirmedEvent event = new ParticipationConfirmedEvent(
            participant.getId(),
            participant.getStage().getId(),
            participant.getUserId(),
            participant.getConfirmationStatus() == ParticipantConfirmationStatus.CONFIRMED,
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishAssignmentSubmitted(RecruitmentStage stage) {
        boolean isLate = stage.getAssignmentDeadline() != null
            && stage.getAssignmentSubmittedAt().isAfter(stage.getAssignmentDeadline());

        AssignmentSubmittedEvent event = new AssignmentSubmittedEvent(
            stage.getId(),
            stage.getRecruitmentProcess().getId(),
            stage.getAssignmentSubmittedAt(),
            stage.getAssignmentDeadline(),
            isLate,
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishDeadlineExceeded(RecruitmentStage stage) {
        DeadlineExceededEvent event = new DeadlineExceededEvent(
            stage.getId(),
            stage.getRecruitmentProcess().getId(),
            stage.getAssignmentDeadline(),
            stage.getAssignmentSubmittedAt(),
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishEvaluationSubmitted(StageEvaluation evaluation) {
        EvaluationSubmittedEvent event = new EvaluationSubmittedEvent(
            evaluation.getId(),
            evaluation.getStage().getId(),
            evaluation.getEvaluatorId(),
            evaluation.getApproved(),
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishStageCompleted(RecruitmentStage stage) {
        StageCompletedEvent event = new StageCompletedEvent(
            stage.getId(),
            stage.getRecruitmentProcess().getId(),
            stage.getStageType(),
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishStageRejected(RecruitmentStage stage, String reason) {
        StageRejectedEvent event = new StageRejectedEvent(
            stage.getId(),
            stage.getRecruitmentProcess().getId(),
            stage.getStageType(),
            reason,
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishNextStageReady(RecruitmentProcess process, RecruitmentStage nextStage) {
        NextStageReadyEvent event = new NextStageReadyEvent(
            process.getId(),
            nextStage.getId(),
            nextStage.getStageType(),
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishRecruitmentCompleted(RecruitmentProcess process) {
        RecruitmentCompletedEvent event = new RecruitmentCompletedEvent(
            process.getId(),
            process.getCandidateName(),
            process.getCandidateEmail(),
            LocalDateTime.now()
        );
        eventPublisher.publishEvent(event);
    }
}
