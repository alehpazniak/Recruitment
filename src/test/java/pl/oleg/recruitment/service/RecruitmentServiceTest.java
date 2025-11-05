package pl.oleg.recruitment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.oleg.recruitment.domain.*;
import pl.oleg.recruitment.dto.*;
import pl.oleg.recruitment.event.RecruitmentEventPublisher;
import pl.oleg.recruitment.mapper.RecruitmentProcessResponseMapper;
import pl.oleg.recruitment.repository.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.oleg.recruitment.domain.RecruitmentStageType.INITIAL_INTERVIEW;

@DisplayName("Recruitment Service Tests")
@ExtendWith(MockitoExtension.class)
public class RecruitmentServiceTest {

    @Mock
    private RecruitmentProcessRepository processRepository;
    @Mock
    private RecruitmentStageRepository stageRepository;
    @Mock
    private StageParticipantRepository participantRepository;
    @Mock
    private QuestionCardRepository questionCardRepository;
    @Mock
    private StageEvaluationRepository evaluationRepository;
    @Mock
    private RecruitmentEventPublisher eventPublisher;
    @Mock
    private RecruitmentProcessResponseMapper recruitmentProcessResponseMapper;
    @InjectMocks
    private RecruitmentService recruitmentService;

    private RecruitmentProcess testProcess;
    private RecruitmentStage testStage;
    private StageParticipant testParticipant;
    private RecruitmentProcessResponse recruitmentProcessResponse;
    private RecruitmentStageResponse recruitmentStageResponse;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testProcess = new RecruitmentProcess();
        testProcess.setId(1L);
        testProcess.setCandidateName("Oleg Pozniak");
        testProcess.setCandidateEmail("olegpozniak@test.com");
        testProcess.setPositionTitle("Java Developer");
        testProcess.setCurrentStatus(StageStatus.PENDING);
        testProcess.setCreatedAt(LocalDateTime.now());

        testStage = new RecruitmentStage();
        testStage.setId(1L);
        testStage.setStageType(INITIAL_INTERVIEW);
        testStage.setStageOrder(1);
        testStage.setStatus(StageStatus.PENDING);
        testStage.setRecruitmentProcess(testProcess);

        testParticipant = new StageParticipant();
        testParticipant.setId(1L);
        testParticipant.setStage(testStage);
        testParticipant.setUserId(101L);
        testParticipant.setUserEmail("recruiter@company.com");
        testParticipant.setUserName("Anna Nowak");
        testParticipant.setIsRequired(true);
        testParticipant.setConfirmationStatus(ParticipantConfirmationStatus.PENDING);

        testStage.setParticipants(List.of(testParticipant));

        recruitmentProcessResponse = new RecruitmentProcessResponse(
            1L,                                 //id
            "Oleg Pozniak",                         //candidateName
            "olegpozniak@test.com",                 //candidateEmail
            "Java Developer",                       //positionTitle
            StageStatus.PENDING,                    //currentStatus
            LocalDateTime.now(),                    //createdAt
            LocalDateTime.now(),                    //completedAt
            List.of(new RecruitmentStageResponse(
                    1L,
                    INITIAL_INTERVIEW,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            ));                                     //stages

    }

    @Test
    @DisplayName("Test 1: Should create recruitment process with all stages")
    void testCreateRecruitmentProcessWithAllStages() {
        // Given
        QuestionCardDTO questionCard = new QuestionCardDTO(
            "Why do you want to work for us?",
            null,
            1,
            "Motivation assessment"
        );

        StageDefinitionDTO stageDefinition = new StageDefinitionDTO(
            INITIAL_INTERVIEW,
            1,
            List.of(101L),
            List.of(questionCard),
            LocalDateTime.now().plusDays(2),
            null
        );

        CreateRecruitmentProcessRequest request = new CreateRecruitmentProcessRequest(
            "Oleg Pozniak",
            "olegpozniak@test.com",
            "Java Developer",
            List.of(stageDefinition)
        );

        when(processRepository.save(any(RecruitmentProcess.class)))
            .thenAnswer(invocation -> {
                RecruitmentProcess process = invocation.getArgument(0);
                process.setId(1L);
                return process;
            });

        when(recruitmentProcessResponseMapper.mapToRecruitmentProcessResponse(any(RecruitmentProcess.class)))
            .thenReturn(recruitmentProcessResponse);

        // When
        RecruitmentProcessResponse response = recruitmentService.createRecruitmentProcess(request);

        // Then
        assertNotNull(response, "Response should not be null");
        assertEquals("Oleg Pozniak", response.candidateName(), "Candidate name should match");
        assertEquals("olegpozniak@test.com", response.candidateEmail(), "Candidate email should match");
        assertEquals("Java Developer", response.positionTitle(), "Position title should match");
        assertEquals(StageStatus.PENDING, response.currentStatus(), "Initial status should be PENDING");
        assertEquals(1, response.stages().size(), "Should have exactly one stage");
        assertEquals(INITIAL_INTERVIEW, response.stages().get(0).stageType(), "Stage type should match");

        verify(processRepository, times(1)).save(any(RecruitmentProcess.class));
        verify(eventPublisher, times(1)).publishRecruitmentCreated(any(RecruitmentProcess.class));
        verify(recruitmentProcessResponseMapper, times(1)).mapToRecruitmentProcessResponse(any(RecruitmentProcess.class));

        // Verify no other unnecessary interactions
        verifyNoMoreInteractions(stageRepository, participantRepository, questionCardRepository);
    }

}
