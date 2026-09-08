package com.wakana.realestateworks.controller.v2;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wakana.realestateworks.dto.v2.LeaveRequestDto;
import com.wakana.realestateworks.dto.v2.LeaveReviewDto;
import com.wakana.realestateworks.dto.v2.response.LeaveBalanceSummaryDto;
import com.wakana.realestateworks.dto.v2.response.LeaveRequestResponseDto;
import com.wakana.realestateworks.dto.v2.response.PagedLeaveRequestDto;
import com.wakana.realestateworks.services.v2.LeaveService;

import java.util.List;

/**
 * REST controller for leave management, scoped per real estate property.
 *
 * All routes are nested under /api/v1/real-estates/{realEstateId}/leaves
 * so that every operation is naturally tied to a specific property.
 */

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/v1/real-estates/{realEstateId}/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    // -------------------------------------------------------------------------
    // GET /api/v1/real-estates/{realEstateId}/leaves/workers/{workerId}/summary
    // Leave balance + last 10 requests for a worker
    // -------------------------------------------------------------------------
    @GetMapping("/workers/{workerId}/summary")
    public ResponseEntity<LeaveBalanceSummaryDto> getLeaveSummary(
            @PathVariable Long realEstateId,
            @PathVariable Long workerId) {
        return ResponseEntity.ok(leaveService.getWorkerLeaveSummary(realEstateId, workerId));
    }

    // -------------------------------------------------------------------------
    // POST /api/v1/real-estates/{realEstateId}/leaves/workers/{workerId}/requests
    // Worker submits a leave request
    // -------------------------------------------------------------------------
    @PostMapping("/workers/{workerId}/requests")
    public ResponseEntity<LeaveRequestResponseDto> submitLeaveRequest(
            @PathVariable Long realEstateId,
            @PathVariable Long workerId,
            @Valid @RequestBody LeaveRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(leaveService.submitLeaveRequest(realEstateId, workerId, dto));
    }

    // -------------------------------------------------------------------------
    // GET /api/v1/real-estates/{realEstateId}/leaves/requests
    //
    // Unified paginated endpoint for all leave requests on a property.
    // Replaces the old /requests/pending endpoint.
    //
    // Query params:
    // status → PENDING | APPROVED | REJECTED | (absent = all)
    // search → firstName, lastName, or full name (partial, case-insensitive)
    // page → page number (default 0)
    // size → page size (default 10)
    //
    // Examples:
    // GET /requests?status=PENDING
    // GET /requests?status=APPROVED&search=Diallo
    // GET /requests?search=Moussa&page=1&size=5
    // GET /requests ← all statuses, page 0
    // -------------------------------------------------------------------------
    @GetMapping("/requests")
    public ResponseEntity<PagedLeaveRequestDto> getLeaveRequests(
            @PathVariable Long realEstateId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(leaveService.getLeaveRequests(realEstateId, status, search, pageable));
    }

    // -------------------------------------------------------------------------
    // PATCH /api/v1/real-estates/{realEstateId}/leaves/requests/{requestId}/review
    // Manager approves or rejects a request
    // -------------------------------------------------------------------------
    @PatchMapping("/requests/{requestId}/review")
    public ResponseEntity<LeaveRequestResponseDto> reviewLeaveRequest(
            @PathVariable Long realEstateId,
            @PathVariable Long requestId,
            @RequestParam Long managerId,
            @Valid @RequestBody LeaveReviewDto reviewDto) {
        return ResponseEntity.ok(leaveService.reviewLeaveRequest(realEstateId, requestId, managerId, reviewDto));
    }

    // -------------------------------------------------------------------------
    // POST /api/v1/real-estates/{realEstateId}/leaves/admin/accrue
    // Manually trigger monthly accrual for this property
    // -------------------------------------------------------------------------
    @PostMapping("/admin/accrue")
    public ResponseEntity<String> triggerAccrual(@PathVariable Long realEstateId) {
        leaveService.accrueMonthlyLeaveForWorkers(realEstateId);
        return ResponseEntity.ok("Accrual déclenché pour les workers de la propriété " + realEstateId);
    }
}

/*
 * @RestController
 * 
 * @RequestMapping("/api/v1/real-estates/{realEstateId}/leaves")
 * 
 * @RequiredArgsConstructor
 * public class LeaveController {
 * 
 * private final LeaveService leaveService;
 * 
 * // -------------------------------------------------------------------------
 * // GET /api/v1/real-estates/{realEstateId}/leaves/workers/{workerId}/summary
 * // Returns leave balance + history for a worker on a specific property
 * // -------------------------------------------------------------------------
 * 
 * @GetMapping("/workers/{workerId}/summary")
 * public ResponseEntity<LeaveBalanceSummaryDto> getLeaveSummary(
 * 
 * @PathVariable Long realEstateId,
 * 
 * @PathVariable Long workerId) {
 * return ResponseEntity.ok(leaveService.getWorkerLeaveSummary(realEstateId,
 * workerId));
 * }
 * 
 * // -------------------------------------------------------------------------
 * // POST
 * /api/v1/real-estates/{realEstateId}/leaves/workers/{workerId}/requests
 * // Worker submits a leave request for a given property
 * // -------------------------------------------------------------------------
 * 
 * @PostMapping("/workers/{workerId}/requests")
 * public ResponseEntity<LeaveRequestResponseDto> submitLeaveRequest(
 * 
 * @PathVariable Long realEstateId,
 * 
 * @PathVariable Long workerId,
 * 
 * @Valid @RequestBody LeaveRequestDto dto) {
 * return ResponseEntity.status(HttpStatus.CREATED)
 * .body(leaveService.submitLeaveRequest(realEstateId, workerId, dto));
 * }
 * 
 * // -------------------------------------------------------------------------
 * // GET /api/v1/real-estates/{realEstateId}/leaves/requests/pending
 * // All pending requests for this property (manager view)
 * // -------------------------------------------------------------------------
 * 
 * @GetMapping("/requests/pending")
 * public ResponseEntity<List<LeaveRequestResponseDto>> getPendingRequests(
 * 
 * @PathVariable Long realEstateId) {
 * return ResponseEntity.ok(leaveService.getAllPendingRequests(realEstateId));
 * }
 * 
 * // -------------------------------------------------------------------------
 * // PATCH
 * /api/v1/real-estates/{realEstateId}/leaves/requests/{requestId}/review
 * // Manager approves or rejects a request
 * // -------------------------------------------------------------------------
 * 
 * @PatchMapping("/requests/{requestId}/review")
 * public ResponseEntity<LeaveRequestResponseDto> reviewLeaveRequest(
 * 
 * @PathVariable Long realEstateId,
 * 
 * @PathVariable Long requestId,
 * 
 * @RequestParam Long managerId,
 * 
 * @Valid @RequestBody LeaveReviewDto reviewDto) {
 * return ResponseEntity.ok(leaveService.reviewLeaveRequest(realEstateId,
 * requestId, managerId, reviewDto));
 * }
 * 
 * // -------------------------------------------------------------------------
 * // POST /api/v1/real-estates/{realEstateId}/leaves/admin/accrue
 * // Manually trigger monthly accrual for workers of this property (testing)
 * // -------------------------------------------------------------------------
 * 
 * @PostMapping("/admin/accrue")
 * public ResponseEntity<String> triggerAccrual(@PathVariable Long realEstateId)
 * {
 * leaveService.accrueMonthlyLeaveForWorkers(realEstateId);
 * return
 * ResponseEntity.ok("Accrual déclenché pour les workers de la propriété " +
 * realEstateId);
 * }
 * }
 */