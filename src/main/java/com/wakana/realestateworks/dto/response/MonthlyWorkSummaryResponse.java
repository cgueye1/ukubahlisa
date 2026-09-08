package com.wakana.realestateworks.dto.response;

import java.util.List;

public record MonthlyWorkSummaryResponse(
    List<DailyWorkSummaryResponse> dailySummaries,
    String totalWorkedTime
) {
}
