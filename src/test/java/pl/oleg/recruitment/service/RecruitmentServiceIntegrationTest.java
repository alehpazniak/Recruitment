//package pl.oleg.recruitment.service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import pl.oleg.recruitment.domain.RecruitmentStageType;
//import pl.oleg.recruitment.domain.StageStatus;
//import pl.oleg.recruitment.dto.*;
//import pl.oleg.recruitment.repository.RecruitmentProcessRepository;
//import pl.oleg.recruitment.repository.RecruitmentStageRepository;
//import pl.oleg.recruitment.repository.StageParticipantRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@Transactional
//class RecruitmentServiceIntegrationTest {
//
//    @Autowired
//    private RecruitmentService recruitmentService;
//
//    @Autowired
//    private RecruitmentProcessRepository processRepository;
//
//    @Autowired
//    private RecruitmentStageRepository stageRepository;
//
//    @Autowired
//    private StageParticipantRepository participantRepository;
//
//    @Test
//    void shouldCreateRecruitmentProcessWithStages() {
//        // Given
//        CreateRecruitmentProcessRequest request = new CreateRecruitmentProcessRequest(
//            "Oleg Pozniak",
//            "olegpozniak@test.com",
//            "Java Developer",
//            List.of(
//                new StageDefinitionDTO(
//                    RecruitmentStageType.INITIAL_INTERVIEW,
//                    1,
//                    List.of(1L),
//                    List.of(
//                        new QuestionCardDTO("Why are you looking for a new job?", null, 1, null),
//                        new QuestionCardDTO("What is your experience with Java?", null, 2, null)
//                    ),
//                    LocalDateTime.now().plusDays(2),
//                    null
//                ),
//                new StageDefinitionDTO(
//                    RecruitmentStageType.ASSIGNMENT,
//                    2,
//                    null,
//                    List.of(
//                        new QuestionCardDTO("Is the code readable?", null, 1, "Readability assessment"),
//                        new QuestionCardDTO("Have DDD practices been applied?", null, 2, "Quality assessment")
//                    ),
//                    null,
//                    LocalDateTime.now().plusDays(7)
//                ),
//                new StageDefinitionDTO(
//                    RecruitmentStageType.TEAM_INTERVIEW,
//                    3,
//                    List.of(2L, 3L, 4L),
//                    List.of(
//                        new QuestionCardDTO("Do you have experience with microservices?", null, 1, null)
//                    ),
//                    LocalDateTime.now().plusDays(10),
//                    null
//                ),
//                new StageDefinitionDTO(
//                    RecruitmentStageType.FINAL_INTERVIEW,
//                    4,
//                    List.of(5L, 6L, 7L),
//                    List.of(
//                        new QuestionCardDTO("What are your expectations?", null, 1, null)
//                    ),
//                    LocalDateTime.now().plusDays(14),
//                    null
//                )
//            )
//        );
//
//        // When
//        RecruitmentProcessResponse response = recruitmentService.createRecruitmentProcess(request);
//
//        // Then
//        assertNotNull(response.id());
//        assertEquals("Oleg Pozniak", response.candidateName());
//        assertEquals("olegpozniak@test.com", response.candidateEmail());
//        assertEquals(4, response.stages().size());
//        assertEquals(StageStatus.PENDING, response.currentStatus());
//
//        // Verify
//        RecruitmentStageResponse firstStage = response.stages().get(0);
//        assertEquals(RecruitmentStageType.INITIAL_INTERVIEW, firstStage.stageType());
//        assertEquals(1, firstStage.participants().size());
//        assertEquals(2, firstStage.questionCards().size());
//    }
//}