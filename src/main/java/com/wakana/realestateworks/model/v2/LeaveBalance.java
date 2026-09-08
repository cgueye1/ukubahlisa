package com.wakana.realestateworks.model.v2;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.wakana.realestateworks.model.User;

import java.time.LocalDateTime;

/**
 * Tracks the leave balance for each worker.
 * One record per worker — updated monthly by the scheduler.
 */
@Data
@Entity
@Table(name = "leave_balances")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false, unique = true)
    private User worker;

    /** Total days accrued (incremented by scheduler each month) */
    private double totalAccruedDays;

    /** Days already used (approved leave requests) */
    private double usedDays;

    /** Days pending approval */
    private double pendingDays;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /** Convenience: remaining days = accrued - used - pending */
    @Transient
    public double getRemainingDays() {
        return totalAccruedDays - usedDays - pendingDays;
    }
}