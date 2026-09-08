package com.wakana.realestateworks.services.impl.v2;

import com.wakana.realestateworks.dto.v2.response.MonthlyAttendanceReportDto;
import com.wakana.realestateworks.dto.v2.response.WorkerAttendanceDto;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.model.WorkerPresence;
import com.wakana.realestateworks.model.v2.LeaveRequest;
import com.wakana.realestateworks.repository.RealEstatePropertyRepository;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.repository.WorkerPresenceRepository;
import com.wakana.realestateworks.repository.v2.LeaveRequestRepository;
import com.wakana.realestateworks.services.v2.AttendanceReportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;




@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceReportServiceImpl implements AttendanceReportService {

    private final WorkerPresenceRepository presenceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final RealEstatePropertyRepository realEstatePropertyRepository;

    @Override
    @Transactional(readOnly = true)
    public MonthlyAttendanceReportDto generateMonthlyReport(Long realEstateId, String monthDate) {

        // ── 1. Validate property ─────────────────────────────────────────────
        RealEstateProperty property = realEstatePropertyRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("Propriété introuvable : " + realEstateId));

        // ── 2. Parse date — derive full month boundaries ─────────────────────
        LocalDate parsed     = parseMonthDate(monthDate);
        LocalDate monthStart = parsed.withDayOfMonth(1);
        LocalDate monthEnd   = parsed.withDayOfMonth(parsed.lengthOfMonth());

        // ── 3. "Today" boundary — cap at monthEnd for past months ────────────
        // pastCutoff = the last day we can evaluate presence/absence on
        LocalDate today      = LocalDate.now();
        LocalDate pastCutoff = today.isBefore(monthEnd) ? today : monthEnd;
        // If the month hasn't started yet pastCutoff could be before monthStart
        boolean monthInFuture = today.isBefore(monthStart);

        // ── 4. Working day counts ────────────────────────────────────────────
        int totalWorkingDays     = countWorkingDays(monthStart, monthEnd);
        int pastWorkingDays      = monthInFuture ? 0 : countWorkingDays(monthStart, pastCutoff);
        int remainingWorkingDays = totalWorkingDays - pastWorkingDays;

        // ── 5. Workers assigned to this property ─────────────────────────────
        List<User> workers = userRepository.findWorkersByRealEstateId(realEstateId);

        // ── 6. Batch-load all presences for the property up to pastCutoff ────
        // No point loading future presences — they don't exist yet
        List<WorkerPresence> allPresences = monthInFuture
                ? Collections.emptyList()
                : presenceRepository.findAllByPropertyInPeriod(realEstateId, monthStart, pastCutoff);

        // Group presence dates by workerId
        Map<Long, Set<LocalDate>> presenceByWorker = allPresences.stream()
                .collect(Collectors.groupingBy(
                        wp -> wp.getWorker().getId(),
                        Collectors.mapping(WorkerPresence::getDate, Collectors.toSet())));

        // ── 7. Build each worker line ────────────────────────────────────────
        List<WorkerAttendanceDto> lines = new ArrayList<>();

        for (User worker : workers) {

            Set<LocalDate> presentDays = presenceByWorker.getOrDefault(worker.getId(), Collections.emptySet());

            int daysPresent = (int) presentDays.stream()
                    .filter(this::isWorkingDay)
                    .count();

            // Approved leaves — clamped to the elapsed period only
            List<LeaveRequest> leaves = monthInFuture
                    ? Collections.emptyList()
                    : leaveRequestRepository.findApprovedLeavesInPeriod(
                            worker.getId(), realEstateId, monthStart, pastCutoff);

            int leaveDays = computeLeaveDaysInPeriod(leaves, monthStart, pastCutoff);

            // Absent = elapsed days not covered by presence or approved leave
            int absentDays = Math.max(0, pastWorkingDays - daysPresent - leaveDays);

            // Deduction uses the full month total (so the % makes sense on the payslip)
            double deductionPercent = totalWorkingDays == 0 ? 0.0
                    : Math.round((absentDays / (double) totalWorkingDays) * 10000.0) / 100.0;

            lines.add(WorkerAttendanceDto.builder()
                    .workerId(worker.getId())
                    .firstName(worker.getPrenom())
                    .lastName(worker.getNom())
                    .totalWorkingDays(totalWorkingDays)
                    .pastWorkingDays(pastWorkingDays)
                    .remainingWorkingDays(remainingWorkingDays)
                    .daysPresent(daysPresent)
                    .leaveDays(leaveDays)
                    .absentDays(absentDays)
                    .salaryDeductionPercent(deductionPercent)
                    .build());
        }

        String reportLabel = monthStart.format(DateTimeFormatter.ofPattern("MM-yyyy"));

        return MonthlyAttendanceReportDto.builder()
                .realEstateId(realEstateId)
                .realEstateName(property.getName())
                .month(reportLabel)
                .totalWorkingDays(totalWorkingDays)
                .pastWorkingDays(pastWorkingDays)
                .remainingWorkingDays(remainingWorkingDays)
                .workers(lines)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private LocalDate parseMonthDate(String monthDate) {
        try {
            return LocalDate.parse(monthDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Format de date invalide. Attendu : dd-MM-yyyy (ex: 01-04-2025). Reçu : " + monthDate);
        }
    }

    private int countWorkingDays(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) return 0;
        int count = 0;
        LocalDate d = start;
        while (!d.isAfter(end)) {
            if (isWorkingDay(d)) count++;
            d = d.plusDays(1);
        }
        return count;
    }

    private boolean isWorkingDay(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        return dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY;
    }

    /**
     * Counts distinct working leave days within [periodStart, periodEnd].
     * Clamps leave requests that span beyond the period boundaries.
     */
    private int computeLeaveDaysInPeriod(List<LeaveRequest> leaves,
                                          LocalDate periodStart,
                                          LocalDate periodEnd) {
        Set<LocalDate> leaveDaySet = new HashSet<>();
        for (LeaveRequest lr : leaves) {
            LocalDate from = lr.getStartDate().isBefore(periodStart) ? periodStart : lr.getStartDate();
            LocalDate to   = lr.getEndDate().isAfter(periodEnd)      ? periodEnd   : lr.getEndDate();
            LocalDate d = from;
            while (!d.isAfter(to)) {
                if (isWorkingDay(d)) leaveDaySet.add(d);
                d = d.plusDays(1);
            }
        }
        return leaveDaySet.size();
    }
}