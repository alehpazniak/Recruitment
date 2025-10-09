package pl.oleg.recruitment.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.oleg.recruitment.domain.RecruitmentStage;
import pl.oleg.recruitment.domain.StageStatus;
import pl.oleg.recruitment.event.RecruitmentEventPublisher;
import pl.oleg.recruitment.repository.RecruitmentStageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RecruitmentScheduledTasks {

    private final RecruitmentStageRepository stageRepository;
    private final RecruitmentEventPublisher eventPublisher;

    @Scheduled(cron = "${scheduler.check-overdue-assignments.cron}")
    public void checkOverdueAssignments() {
        log.info("Checking for overdue assignments");

        List<RecruitmentStage> overdueStages = stageRepository.findOverdueAssignments(
            StageStatus.IN_PROGRESS,
            LocalDateTime.now()
        );

        for (RecruitmentStage stage : overdueStages) {
            log.warn("Assignment stage {} is overdue. Deadline was: {}",
                stage.getId(),
                stage.getAssignmentDeadline());

            eventPublisher.publishDeadlineExceeded(stage);
        }

        log.info("Found {} overdue assignments", overdueStages.size());
    }

    @Scheduled(cron = "${scheduler.send-upcoming-meeting-reminders.cron}")
    public void sendUpcomingMeetingReminders() {
        log.info("Sending reminders for upcoming meetings");

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime tomorrowStart = tomorrow.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrowEnd = tomorrow.withHour(23).withMinute(59).withSecond(59);

        List<RecruitmentStage> upcomingStages = stageRepository.findAll().stream()
            .filter(stage -> stage.getScheduledAt() != null)
            .filter(stage -> stage.getScheduledAt().isAfter(tomorrowStart)
                && stage.getScheduledAt().isBefore(tomorrowEnd))
            .filter(stage -> stage.getStatus() == StageStatus.AWAITING_CONFIRMATIONS
                || stage.getStatus() == StageStatus.IN_PROGRESS)
            .toList();

        for (RecruitmentStage stage : upcomingStages) {
            log.info("Sending reminder for stage {} scheduled at {}",
                stage.getId(),
                stage.getScheduledAt());
        }

        log.info("Sent {} meeting reminders", upcomingStages.size());
    }
}
