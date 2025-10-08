package pl.oleg.recruitment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.oleg.recruitment.dto.*;
import pl.oleg.recruitment.service.RecruitmentService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping("/processes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RecruitmentProcessResponse> createRecruitmentProcess(
        @Valid @RequestBody CreateRecruitmentProcessRequest request) {
        RecruitmentProcessResponse response = recruitmentService.createRecruitmentProcess(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/processes/{processId}")
    public ResponseEntity<RecruitmentProcessResponse> getRecruitmentProcess(@PathVariable Long processId) {
        RecruitmentProcessResponse response = recruitmentService.getRecruitmentProcess(processId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stages/{stageId}/start")
    public ResponseEntity<RecruitmentStageResponse> startStage(@PathVariable Long stageId) {
        RecruitmentStageResponse response = recruitmentService.startStage(stageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stages/{stageId}")
    public ResponseEntity<RecruitmentStageResponse> getStage(@PathVariable Long stageId) {
        RecruitmentStageResponse response = recruitmentService.getStage(stageId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stages/confirm-participation")
    public ResponseEntity<ParticipantResponse> confirmParticipation(
        @Valid @RequestBody ConfirmParticipationRequest request) {
        ParticipantResponse response = recruitmentService.confirmParticipation(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stages/{stageId}/submit-assignment")
    public ResponseEntity<RecruitmentStageResponse> submitAssignment(@PathVariable Long stageId) {
        RecruitmentStageResponse response = recruitmentService.submitAssignment(stageId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stages/approve-deadline-override")
    public ResponseEntity<RecruitmentStageResponse> approveDeadlineOverride(
        @Valid @RequestBody ApproveDeadlineOverrideRequest request) {
        RecruitmentStageResponse response = recruitmentService.approveDeadlineOverride(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stages/evaluations")
    public ResponseEntity<EvaluationResponse> submitEvaluation(@Valid @RequestBody SubmitEvaluationRequest request) {
        EvaluationResponse response = recruitmentService.submitEvaluation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/stages/{stageId}/questions")
    public ResponseEntity<List<QuestionCardResponse>> getStageQuestions(@PathVariable Long stageId) {
        List<QuestionCardResponse> response = recruitmentService.getStageQuestions(stageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stages/{stageId}/evaluations")
    public ResponseEntity<List<EvaluationResponse>> getStageEvaluations(@PathVariable Long stageId) {
        List<EvaluationResponse> response = recruitmentService.getStageEvaluations(stageId);
        return ResponseEntity.ok(response);
    }
}
