package pl.oleg.recruitment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "question_evaluations")
public class QuestionEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private StageEvaluation evaluation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_card_id", nullable = false)
    private QuestionCard questionCard;

    @Column(length = 2000)
    private String answer;

    private Integer score;

    @Column(length = 1000)
    private String comment;
}
