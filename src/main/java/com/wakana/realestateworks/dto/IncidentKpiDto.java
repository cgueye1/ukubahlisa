package com.wakana.realestateworks.dto;

public class IncidentKpiDto {
    private String date;       // ex: "2025-06-15"
    private Long count;        // nombre d'incidents
    private String statusLabel; // ex: "Aucun incident", "Quelques incidents", "Beaucoup d'incidents"
    private String color;       // ex: "#00AA00" (vert), "#FFA500" (orange), "#FF0000" (rouge)

    public IncidentKpiDto(String date, Long count, String statusLabel, String color) {
        this.date = date;
        this.count = count;
        this.statusLabel = statusLabel;
        this.color = color;
    }

    // getters et setters (ou @Data lombok)
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
