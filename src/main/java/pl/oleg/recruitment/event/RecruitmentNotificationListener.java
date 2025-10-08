package pl.oleg.recruitment.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.oleg.recruitment.domain.event.*;

@Slf4j
@Component
public class RecruitmentNotificationListener {

    @EventListener
    public void handleConfirmationsRequested(ConfirmationsRequestedEvent event) {
        log.info("Sending confirmation requests to participants for stage {}", event.stageId());
    }

    @EventListener
    public void handleDeadlineExceeded(DeadlineExceededEvent event) {
        log.warn("Assignment deadline exceeded for stage {}", event.stageId());
    }

    @EventListener
    public void handleStageCompleted(StageCompletedEvent event) {
        log.info("Stage {} completed successfully", event.stageId());
    }

    @EventListener
    public void handleStageRejected(StageRejectedEvent event) {
        log.info("Stage {} rejected: {}", event.stageId(), event.reason());
    }

    @EventListener
    public void handleRecruitmentCompleted(RecruitmentCompletedEvent event) {
        log.info("Recruitment process {} completed successfully", event.processId());
    }
}
