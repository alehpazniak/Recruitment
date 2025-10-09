package pl.oleg.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.oleg.recruitment.domain.*;
import pl.oleg.recruitment.dto.*;
import pl.oleg.recruitment.event.RecruitmentEventPublisher;
import pl.oleg.recruitment.exeption.ResourceNotFoundException;
import pl.oleg.recruitment.mapper.RecruitmentProcessResponseMapper;
import pl.oleg.recruitment.repository.*;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class RecruitmentService {

    private final RecruitmentProcessRepository processRepository;
    private final RecruitmentStageRepository stageRepository;
    private final StageParticipantRepository participantRepository;
    private final QuestionCardRepository questionCardRepository;
    private final StageEvaluationRepository evaluationRepository;
    private final RecruitmentEventPublisher eventPublisher;
    private final RecruitmentProcessResponseMapper recruitmentProcessResponseMapper;

    public RecruitmentProcessResponse createRecruitmentProcess(CreateRecruitmentProcessRequest request) {
        validateStageDefinitions(request.stageDefinitions());
        RecruitmentProcess process = new RecruitmentProcess();
        process.setCandidateName(request.candidateName());
        process.setCandidateEmail(request.candidateEmail());
        process.setPositionTitle(request.positionTitle());
        process.setCurrentStatus(StageStatus.PENDING);
        process.setCreatedAt(LocalDateTime.now());
        for (StageDefinitionDTO stageDef : request.stageDefinitions()) {
            RecruitmentStage stage = createStage(process, stageDef);
            process.getStages().add(stage);
        }
        if (!process.getStages().isEmpty()) {
            process.setCurrentStage(process.getStages().get(0));
        }
        RecruitmentProcess savedProcess = processRepository.save(process);
        eventPublisher.publishRecruitmentCreated(savedProcess);
        return recruitmentProcessResponseMapper.mapToRecruitmentProcessResponse(savedProcess);
    }

    private RecruitmentStage createStage(RecruitmentProcess process, StageDefinitionDTO stageDef) {
        RecruitmentStage stage = new RecruitmentStage();
        stage.setRecruitmentProcess(process);
        stage.setStageType(stageDef.stageType());
        stage.setStageOrder(stageDef.stageOrder());
        stage.setStatus(StageStatus.PENDING);
        stage.setScheduledAt(stageDef.scheduledAt());
        if (stageDef.stageType() == RecruitmentStageType.ASSIGNMENT) {
            stage.setAssignmentDeadline(stageDef.assignmentDeadline());
        }
        if (stageDef.participantUserIds() != null) {
            for (Long userId : stageDef.participantUserIds()) {
                StageParticipant participant = new StageParticipant();
                participant.setStage(stage);
                participant.setUserId(userId);
                participant.setIsRequired(true);
                participant.setConfirmationStatus(ParticipantConfirmationStatus.PENDING);
                stage.getParticipants().add(participant);
            }
        }
        if (stageDef.questionCards() != null) {
            for (QuestionCardDTO cardDto : stageDef.questionCards()) {
                QuestionCard card = new QuestionCard();
                card.setStage(stage);
                card.setQuestion(cardDto.question());
                card.setExpectedAnswer(cardDto.expectedAnswer());
                card.setOrderIndex(cardDto.orderIndex());
                card.setEvaluationCriteria(cardDto.evaluationCriteria());
                card.setIsActive(true);
                stage.getQuestionCards().add(card);
            }
        }
        return stage;
    }

    public RecruitmentStageResponse startStage(Long stageId) {
        RecruitmentStage stage = stageRepository.findByIdWithParticipants(stageId)
            .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        if (stage.getStatus() != StageStatus.PENDING) {
            throw new IllegalStateException("Stage already started or completed");
        }
        if (!stage.getParticipants().isEmpty()) {
            stage.setStatus(StageStatus.AWAITING_CONFIRMATIONS);
            eventPublisher.publishConfirmationsRequested(stage);
        } else {
            stage.setStatus(StageStatus.IN_PROGRESS);
            stage.setStartedAt(LocalDateTime.now());
            eventPublisher.publishStageStarted(stage);
        }
        return recruitmentProcessResponseMapper.mapToStageResponse(stageRepository.save(stage));
    }

    public ParticipantResponse confirmParticipation(ConfirmParticipationRequest request) {
        StageParticipant participant = participantRepository.findByStageIdAndUserId(request.stageId(), request.userId())
            .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));
        if (participant.getStage().getStatus() != StageStatus.AWAITING_CONFIRMATIONS) {
            throw new IllegalStateException("Stage is not awaiting confirmations");
        }
        participant.setConfirmationStatus(
            request.confirmed() ? ParticipantConfirmationStatus.CONFIRMED
                : ParticipantConfirmationStatus.DECLINED
        );
        participant.setConfirmedAt(LocalDateTime.now());
        participant.setConfirmationNote(request.note());

        StageParticipant savedParticipant = participantRepository.save(participant);
        checkAndStartStageIfAllConfirmed(request.stageId());
        eventPublisher.publishParticipationConfirmed(savedParticipant);
        return recruitmentProcessResponseMapper.mapToParticipantResponse(savedParticipant);
    }

    private void checkAndStartStageIfAllConfirmed(Long stageId) {
        List<StageParticipant> unconfirmed = participantRepository.findUnconfirmedRequiredParticipants(stageId);

        if (unconfirmed.isEmpty()) {
            RecruitmentStage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
            boolean anyDeclined = stage.getParticipants().stream()
                .filter(StageParticipant::getIsRequired)
                .anyMatch(p -> p.getConfirmationStatus() == ParticipantConfirmationStatus.DECLINED);

            if (anyDeclined) {
                stage.setStatus(StageStatus.REJECTED);
                stage.setCompletedAt(LocalDateTime.now());
                eventPublisher.publishStageRejected(stage, "Participant declined");
            } else {
                stage.setStatus(StageStatus.IN_PROGRESS);
                stage.setStartedAt(LocalDateTime.now());
                eventPublisher.publishStageStarted(stage);
            }
            stageRepository.save(stage);
        }
    }

    public RecruitmentStageResponse submitAssignment(Long stageId) {
        RecruitmentStage stage = stageRepository.findById(stageId)
            .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        if (stage.getStageType() != RecruitmentStageType.ASSIGNMENT) {
            throw new IllegalStateException("This is not an assignment stage");
        }
        if (stage.getStatus() != StageStatus.IN_PROGRESS) {
            throw new IllegalStateException("Stage is not in progress");
        }
        stage.setAssignmentSubmittedAt(LocalDateTime.now());
        stage.setStatus(StageStatus.AWAITING_EVALUATION);
        if (stage.getAssignmentDeadline() != null
            && stage.getAssignmentSubmittedAt().isAfter(stage.getAssignmentDeadline())) {
            eventPublisher.publishDeadlineExceeded(stage);
        }
        RecruitmentStage savedStage = stageRepository.save(stage);
        eventPublisher.publishAssignmentSubmitted(savedStage);
        return recruitmentProcessResponseMapper.mapToStageResponse(savedStage);
    }

    public RecruitmentStageResponse approveDeadlineOverride(ApproveDeadlineOverrideRequest request) {
        RecruitmentStage stage = stageRepository.findById(request.stageId())
            .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        stage.setDeadlineOverrideApproved(request.approved());
        if (!request.approved()) {
            stage.setStatus(StageStatus.REJECTED);
            stage.setCompletedAt(LocalDateTime.now());
            eventPublisher.publishStageRejected(stage, request.reason());
        }
        return recruitmentProcessResponseMapper.mapToStageResponse(stageRepository.save(stage));
    }

    public EvaluationResponse submitEvaluation(SubmitEvaluationRequest request) {
        RecruitmentStage stage = stageRepository.findByIdWithQuestionCards(request.stageId())
            .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        if (stage.getStatus() != StageStatus.AWAITING_EVALUATION
            && stage.getStatus() != StageStatus.IN_PROGRESS) {
            throw new IllegalStateException("Stage is not ready for evaluation");
        }
        evaluationRepository.findByStageIdAndEvaluatorId(request.stageId(), request.evaluatorId())
            .ifPresent(e -> {
                throw new IllegalStateException("Evaluator already submitted evaluation");
            });
        StageEvaluation evaluation = new StageEvaluation();
        evaluation.setStage(stage);
        evaluation.setEvaluatorId(request.evaluatorId());
        evaluation.setEvaluatorName("User " + request.evaluatorId());
        evaluation.setApproved(request.approved());
        evaluation.setFeedback(request.feedback());
        evaluation.setEvaluatedAt(LocalDateTime.now());
        for (QuestionEvaluationDTO qeDto : request.questionEvaluations()) {
            QuestionCard card = stage.getQuestionCards().stream()
                .filter(qc -> qc.getId().equals(qeDto.questionCardId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Question card not found"));

            QuestionEvaluation qe = new QuestionEvaluation();
            qe.setEvaluation(evaluation);
            qe.setQuestionCard(card);
            qe.setAnswer(qeDto.answer());
            qe.setScore(qeDto.score());
            qe.setComment(qeDto.comment());

            evaluation.getQuestionEvaluations().add(qe);
        }
        StageEvaluation savedEvaluation = evaluationRepository.save(evaluation);
        checkAndCompleteStageIfAllEvaluated(request.stageId());
        eventPublisher.publishEvaluationSubmitted(savedEvaluation);
        return recruitmentProcessResponseMapper.mapToEvaluationResponse(savedEvaluation);
    }

    private void checkAndCompleteStageIfAllEvaluated(Long stageId) {
        RecruitmentStage stage = stageRepository.findByIdWithParticipants(stageId)
            .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));

        long expectedEvaluations = stage.getParticipants().stream()
            .filter(p -> p.getConfirmationStatus() == ParticipantConfirmationStatus.CONFIRMED)
            .count();
        long actualEvaluations = evaluationRepository.countEvaluationsByStageId(stageId);

        if (expectedEvaluations == actualEvaluations) {
            long rejections = evaluationRepository.countRejectionsByStageId(stageId);
            if (rejections > 0) {
                stage.setStatus(StageStatus.REJECTED);
                stage.setCompletedAt(LocalDateTime.now());
                eventPublisher.publishStageRejected(stage, "Negative evaluation received");
            } else {
                stage.setStatus(StageStatus.COMPLETED);
                stage.setCompletedAt(LocalDateTime.now());
                eventPublisher.publishStageCompleted(stage);
                moveToNextStage(stage.getRecruitmentProcess().getId());
            }
            stageRepository.save(stage);
        }
    }

    private void moveToNextStage(Long processId) {
        RecruitmentProcess process = processRepository.findByIdWithStages(processId)
            .orElseThrow(() -> new ResourceNotFoundException("Process not found"));

        RecruitmentStage currentStage = process.getCurrentStage();

        Optional<RecruitmentStage> nextStage = process.getStages().stream()
            .filter(s -> s.getStageOrder() > currentStage.getStageOrder())
            .min(Comparator.comparing(RecruitmentStage::getStageOrder));

        if (nextStage.isPresent()) {
            process.setCurrentStage(nextStage.get());
            process.setCurrentStatus(StageStatus.PENDING);
            processRepository.save(process);
            eventPublisher.publishNextStageReady(process, nextStage.get());
        } else {
            process.setCurrentStatus(StageStatus.COMPLETED);
            process.setCompletedAt(LocalDateTime.now());
            processRepository.save(process);
            eventPublisher.publishRecruitmentCompleted(process);
        }
    }

    @Transactional(readOnly = true)
    public RecruitmentProcessResponse getRecruitmentProcess(Long processId) {
        RecruitmentProcess process = processRepository.findByIdWithStages(processId)
            .orElseThrow(() -> new ResourceNotFoundException("Process not found"));
        return recruitmentProcessResponseMapper.mapToRecruitmentProcessResponse(process);
    }

    @Transactional(readOnly = true)
    public RecruitmentStageResponse getStage(Long stageId) {
        RecruitmentStage stage = stageRepository.findById(stageId)
            .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
        return recruitmentProcessResponseMapper.mapToStageResponse(stage);
    }

    @Transactional(readOnly = true)
    public List<QuestionCardResponse> getStageQuestions(Long stageId) {
        List<QuestionCard> cards = questionCardRepository
            .findByStageIdAndIsActiveTrueOrderByOrderIndexAsc(stageId);
        return cards.stream()
            .map(recruitmentProcessResponseMapper::mapToQuestionCardResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluationResponse> getStageEvaluations(Long stageId) {
        List<StageEvaluation> evaluations = evaluationRepository.findByStageId(stageId);
        return evaluations.stream()
            .map(recruitmentProcessResponseMapper::mapToEvaluationResponse)
            .toList();
    }

    private void validateStageDefinitions(List<StageDefinitionDTO> definitions) {
        if (definitions == null || definitions.isEmpty()) {
            throw new IllegalArgumentException("At least one stage definition required");
        }
        Set<Integer> orders = new HashSet<>();
        for (StageDefinitionDTO def : definitions) {
            if (!orders.add(def.stageOrder())) {
                throw new IllegalArgumentException("Duplicate stage order: " + def.stageOrder());
            }
        }

        for (StageDefinitionDTO def : definitions) {
            if (def.stageType() == RecruitmentStageType.ASSIGNMENT
                && def.assignmentDeadline() == null) {
                throw new IllegalArgumentException("Assignment stage requires deadline");
            }
        }
    }
}
