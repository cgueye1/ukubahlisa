package com.wakana.realestateworks.controller.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wakana.realestateworks.dto.v2.response.MonthlyAttendanceReportDto;
import com.wakana.realestateworks.services.v2.AttendanceReportService;

/**
 * REST controller for attendance reports.
 *
 * Base path: /api/v1/attendance
 */
@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceReportController {

    private final AttendanceReportService attendanceReportService;

    // -------------------------------------------------------------------------
    // GET /api/v1/attendance/real-estates/{realEstateId}/report?month=01-04-2025
    // -------------------------------------------------------------------------

    /**
     * Generates the monthly attendance report for all workers assigned to a
     * specific real estate property.
     *
     * @param realEstateId the ID of the real estate property
     * @param month        date in format "dd-MM-yyyy" (ex: 01-04-2025)
     *                     The day part is ignored — the full month is used.
     *
     * Response example:
     * {
     *   "realEstateId": 3,
     *   "realEstateName": "Résidence Les Acacias",
     *   "month": "04-2025",
     *   "totalWorkingDays": 22,
     *   "workers": [
     *     {
     *       "workerId": 5,
     *       "firstName": "Moussa",
     *       "lastName": "Sow",
     *       "totalWorkingDays": 22,
     *       "daysPresent": 18,
     *       "leaveDays": 1,
     *       "absentDays": 3,
     *       "salaryDeductionPercent": 13.64
     *     }
     *   ]
     * }
     */
    @GetMapping("/real-estates/{realEstateId}/report")
    public ResponseEntity<MonthlyAttendanceReportDto> getMonthlyReport(
            @PathVariable Long realEstateId,
            @RequestParam String month) {
        return ResponseEntity.ok(attendanceReportService.generateMonthlyReport(realEstateId, month));
    }
}