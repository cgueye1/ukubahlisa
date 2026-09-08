package com.wakana.realestateworks.dto.v2.response;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAttendanceReportDto {

    private Long realEstateId;
    private String realEstateName;

    /** Format: MM-yyyy (ex: "04-2026") */
    private String month;

    /** Total working days (Mon–Fri) in the full month */
    private int totalWorkingDays;

    /** Working days already elapsed (up to today, or full month if month is over) */
    private int pastWorkingDays;

    /** Working days still to come = totalWorkingDays - pastWorkingDays */
    private int remainingWorkingDays;

    private List<WorkerAttendanceDto> workers;
}