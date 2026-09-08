package com.wakana.realestateworks.repository.v2;

import com.wakana.realestateworks.enums.LeaveRequestStatus;
import com.wakana.realestateworks.model.v2.LeaveRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long>,
                JpaSpecificationExecutor<LeaveRequest> {   // ← enables Specification queries
 
    /** Last 10 requests for a worker on a property — for summary widget */
    List<LeaveRequest> findTop10ByWorkerIdAndRealEstatePropertyIdOrderByCreatedAtDesc(
            Long workerId, Long realEstateId);
 
    /** Approved leaves overlapping a period — for monthly report */
    @Query("""
            SELECT lr FROM LeaveRequest lr
            WHERE lr.worker.id             = :workerId
              AND lr.realEstateProperty.id = :realEstateId
              AND lr.status                = 'APPROVED'
              AND lr.startDate            <= :monthEnd
              AND lr.endDate             >= :monthStart
            """)
    List<LeaveRequest> findApprovedLeavesInPeriod(
            @Param("workerId") Long workerId,
            @Param("realEstateId") Long realEstateId,
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd);
 
    /** Overlap check before creating a new request */
    @Query("""
            SELECT COUNT(lr) > 0 FROM LeaveRequest lr
            WHERE lr.worker.id             = :workerId
              AND lr.realEstateProperty.id = :realEstateId
              AND lr.status               IN ('PENDING', 'APPROVED')
              AND lr.startDate           <= :endDate
              AND lr.endDate             >= :startDate
            """)
    boolean existsOverlappingRequest(
            @Param("workerId") Long workerId,
            @Param("realEstateId") Long realEstateId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
 