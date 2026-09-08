package com.wakana.realestateworks.model.v2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wakana.realestateworks.enums.LeaveRequestStatus;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.model.User;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;




/**
 * Represents a leave request submitted by a worker.
 * Scoped to a specific real estate property so managers
 * can filter and validate per site.
 */
@Data
@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    @JsonIgnore
    private User worker;

    /** The property this leave request is scoped to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_estate_id", nullable = false)
    @JsonIgnore
    private RealEstateProperty realEstateProperty;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    /** Number of working days in the requested range (computed on creation) */
    private double requestedDays;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveRequestStatus status = LeaveRequestStatus.PENDING;

    /** Mandatory when status = REJECTED */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String managerComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    @JsonIgnore
    private User reviewedBy;

    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}