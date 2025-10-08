package pl.oleg.recruitment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "stage_participants")
public class StageParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    private RecruitmentStage stage;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantConfirmationStatus confirmationStatus;

    @Column(nullable = false)
    private Boolean isRequired;

    private LocalDateTime confirmedAt;

    @Column(length = 500)
    private String confirmationNote;
}
