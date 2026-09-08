package com.wakana.realestateworks.services.v2;

import java.util.List;

import com.wakana.realestateworks.dto.v2.LeaveRequestDto;
import com.wakana.realestateworks.dto.v2.LeaveReviewDto;
import com.wakana.realestateworks.dto.v2.response.LeaveBalanceSummaryDto;
import com.wakana.realestateworks.dto.v2.response.LeaveRequestResponseDto;
import com.wakana.realestateworks.dto.v2.response.PagedLeaveRequestDto;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LeaveService {

    void accrueMonthlyLeaveForWorkers(Long realEstateId);

    void accrueMonthlyLeaveForAllWorkers();

    /**
     * Leave balance + last 10 requests for a worker on a property.
     */
    LeaveBalanceSummaryDto getWorkerLeaveSummary(Long realEstateId, Long workerId);

    LeaveRequestResponseDto submitLeaveRequest(Long realEstateId, Long workerId, LeaveRequestDto dto);

    LeaveRequestResponseDto reviewLeaveRequest(Long realEstateId, Long requestId, Long managerId,
            LeaveReviewDto reviewDto);

    /**
     * Paginated leave requests for a property.
     *
     * @param realEstateId the property
     * @param status       filter by status — null returns all statuses
     * @param search       optional search on worker firstName / lastName
     * @param pageable     pagination + sorting
     */
    PagedLeaveRequestDto getLeaveRequests(Long realEstateId, String status, String search, Pageable pageable);
}

/*
 * *
 * public interface LeaveService {
 * 
 * 
 * void accrueMonthlyLeaveForWorkers(Long realEstateId);
 * 
 * 
 * void accrueMonthlyLeaveForAllWorkers();
 * 
 * 
 * LeaveBalanceSummaryDto getWorkerLeaveSummary(Long realEstateId, Long
 * workerId);
 * 
 * 
 * LeaveRequestResponseDto submitLeaveRequest(Long realEstateId, Long workerId,
 * LeaveRequestDto dto);
 * 
 * 
 * LeaveRequestResponseDto reviewLeaveRequest(Long realEstateId, Long requestId,
 * Long managerId, LeaveReviewDto reviewDto);
 * 
 * 
 * List<LeaveRequestResponseDto> getAllPendingRequests(Long realEstateId);
 * }
 */