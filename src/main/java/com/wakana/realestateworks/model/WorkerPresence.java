package com.wakana.realestateworks.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "worker_presences", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "worker_id", "date" }, name = "unique_worker_date")
})
public class WorkerPresence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    private LocalDate date;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    @OneToMany(mappedBy = "presence", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PresenceLog> logs;
}
