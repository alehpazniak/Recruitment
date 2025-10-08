package pl.oleg.recruitment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "question_cards")
public class QuestionCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    private RecruitmentStage stage;

    @Column(nullable = false, length = 1000)
    private String question;

    @Column(length = 2000)
    private String expectedAnswer;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(length = 500)
    private String evaluationCriteria;
}
