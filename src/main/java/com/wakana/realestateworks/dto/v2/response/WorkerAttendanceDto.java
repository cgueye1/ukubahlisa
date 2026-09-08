package com.wakana.realestateworks.dto.v2.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Attendance detail for a single worker in a given month.
 *
 * Working days are split into:
 * - pastWorkingDays : working days already elapsed (up to today or month end)
 * - remainingWorkingDays : working days not yet reached in the month
 *
 * Absent days are computed only on elapsed days — future days are NOT counted
 * as absent.
 * Salary deduction is based solely on (absentDays / totalWorkingDays).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerAttendanceDto {

    private Long workerId;
    private String firstName;
    private String lastName;

    /** Total working days (Mon–Fri) in the full month */
    private int totalWorkingDays;

    /**
     * Working days that have already passed (min of today and month end).
     * Absence can only be determined for these days.
     */
    private int pastWorkingDays;

    /**
     * Working days still to come in the month.
     * = totalWorkingDays - pastWorkingDays
     */
    private int remainingWorkingDays;

    /** Days the worker was actually present (has a WorkerPresence record) */
    private int daysPresent;

    /** Approved leave days that fall within the elapsed period */
    private int leaveDays;

    /**
     * Confirmed absent days = pastWorkingDays - daysPresent - leaveDays.
     * Only computed on elapsed days — never includes future days.
     */
    private int absentDays;

    /**
     * Salary deduction % = (absentDays / totalWorkingDays) * 100.
     * Based on the full month total so the rate is stable as the month progresses.
     */
    private double salaryDeductionPercent;
}