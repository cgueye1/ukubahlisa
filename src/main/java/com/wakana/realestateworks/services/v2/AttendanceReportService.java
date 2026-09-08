package com.wakana.realestateworks.services.v2;

import com.wakana.realestateworks.dto.v2.response.MonthlyAttendanceReportDto;



public interface AttendanceReportService {

    /**
     * Generates the monthly attendance report for all workers of a specific
     * real estate property.
     *
     * @param realEstateId the property to scope the report to
     * @param monthDate    a date string in "dd-MM-yyyy" format (ex: "01-04-2025")
     */
    MonthlyAttendanceReportDto generateMonthlyReport(Long realEstateId, String monthDate);
}