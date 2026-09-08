package com.wakana.realestateworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import com.wakana.realestateworks.model.WorkerPresence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkerPresenceRepository extends JpaRepository<WorkerPresence, Long> {

    Optional<WorkerPresence> findByWorkerIdAndDate(Long workerId, LocalDate date);

    long countByWorker_AssignedCompany_IdAndDateAndCheckInTimeIsNotNull(Long propertyId, LocalDate date);

    @Query("""
                SELECT COUNT(wp) FROM WorkerPresence wp
                WHERE (
                    wp.worker.assignedCompany.promoter.id = :promoterId
                    OR wp.worker.assignedCompany.moa.id = :promoterId
                    OR wp.worker.assignedCompany.manager.id = :promoterId
                )
                AND wp.date = :date
                AND wp.checkInTime IS NOT NULL
            """)
    long countByWorker_AssignedCompany_Promoter_IdAndDateAndCheckInTimeIsNotNull(
            @Param("promoterId") Long promoterId,
            @Param("date") LocalDate date);

    List<WorkerPresence> findByWorkerIdAndDateBetween(Long workerId, LocalDate startDate, LocalDate endDate);

    // v2

    /**
     * Presence records for a specific worker on a specific property in a date
     * range.
     * Filters via worker.assignedCompany (no direct FK on WorkerPresence).
     */
    @Query("""
            SELECT wp FROM WorkerPresence wp
            WHERE wp.worker.id                   = :workerId
              AND wp.worker.assignedCompany.id   = :realEstateId
              AND wp.date                       >= :from
              AND wp.date                       <= :to
            """)
    List<WorkerPresence> findByWorkerAndPropertyInPeriod(
            @Param("workerId") Long workerId,
            @Param("realEstateId") Long realEstateId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    /**
     * All presence records for ALL workers assigned to a given property in a date
     * range.
     * Batch load for the monthly report — avoids N+1.
     * Filters via worker.assignedCompany.
     */
    @Query("""
            SELECT wp FROM WorkerPresence wp
            WHERE wp.worker.assignedCompany.id = :realEstateId
              AND wp.date                     >= :from
              AND wp.date                     <= :to
            """)
    List<WorkerPresence> findAllByPropertyInPeriod(
            @Param("realEstateId") Long realEstateId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

}
