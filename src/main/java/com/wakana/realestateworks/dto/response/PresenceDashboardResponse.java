package com.wakana.realestateworks.dto.response;


public class PresenceDashboardResponse {

    private int daysPresent;
    private long totalWorkedHours;

    public PresenceDashboardResponse(int daysPresent, long totalWorkedHours) {
        this.daysPresent = daysPresent;
        this.totalWorkedHours = totalWorkedHours;
    }

    public int getDaysPresent() {
        return daysPresent;
    }

    public void setDaysPresent(int daysPresent) {
        this.daysPresent = daysPresent;
    }

    public long getTotalWorkedHours() {
        return totalWorkedHours;
    }

    public void setTotalWorkedHours(long totalWorkedHours) {
        this.totalWorkedHours = totalWorkedHours;
    }
}
