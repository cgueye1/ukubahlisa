package com.wakana.realestateworks.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "presence_logs")
public class PresenceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presence_id")
    @JsonIgnore
    private WorkerPresence presence;

    private double latitude;
    private double longitude;
}
