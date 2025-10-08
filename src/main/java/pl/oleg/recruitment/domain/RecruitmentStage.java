package pl.oleg.recruitment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "recruitment_stages")
public class RecruitmentStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_process_id", nullable = false)
    private RecruitmentProcess recruitmentProcess;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentStageType stageType;

    @Column(nullable = false)
    private Integer stageOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StageStatus status;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StageParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionCard> questionCards = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StageEvaluation> evaluations = new ArrayList<>();

    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime assignmentDeadline;
    private LocalDateTime assignmentSubmittedAt;
    private Boolean deadlineOverrideApproved;

    @Column(length = 2000)
    private String notes;
}
