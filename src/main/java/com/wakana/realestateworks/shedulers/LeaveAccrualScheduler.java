package com.wakana.realestateworks.shedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wakana.realestateworks.services.v2.LeaveService;

/**
 * Scheduler responsible for accruing 1 leave day per worker each month.
 *
 * Cron: "0 0 1 1 * ?" → runs at 00:00 on the 1st day of every month.
 * Adjust timezone in @Scheduled if needed.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeaveAccrualScheduler {

    private final LeaveService leaveService;

    /**
     * Fires at midnight on the 1st of every month.
     * Cron expression: second minute hour dayOfMonth month dayOfWeek
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void accrueMonthlyLeave() {
        log.info("=== [SCHEDULER] Monthly leave accrual triggered ===");
        try {
            leaveService.accrueMonthlyLeaveForAllWorkers();
            log.info("=== [SCHEDULER] Monthly leave accrual completed ===");
        } catch (Exception e) {
            log.error("=== [SCHEDULER] Error during monthly leave accrual: {} ===", e.getMessage(), e);
        }
    }
}