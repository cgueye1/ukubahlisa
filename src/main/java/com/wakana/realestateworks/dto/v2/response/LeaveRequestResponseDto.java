package com.wakana.realestateworks.dto.v2.response;

import com.wakana.realestateworks.enums.LeaveRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponseDto {

    private Long id;

    private Long workerId;
    private String workerFirstName;
    private String workerLastName;

    private Long realEstateId;
    private String realEstateName;

    private LocalDate startDate;
    private LocalDate endDate;
    private double requestedDays;
    private String reason;
    private LeaveRequestStatus status;
    private String managerComment;
    private String reviewedByName;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}