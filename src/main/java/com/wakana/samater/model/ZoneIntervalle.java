package com.wakana.samater.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "zoneinterval")
public class ZoneIntervalle {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    
    private Double duree;
    private int voie;
    
    @ManyToOne
    @JoinColumn(name = "gare_depart_id") 
    private Gare gareDepart;
    
    @ManyToOne
    @JoinColumn(name = "gare_arrive_id") 
    private Gare gareArrive;
    
    private boolean m_fin_de_zone;
    
    @ManyToOne
    @JoinColumn(name = "zone_id", referencedColumnName = "id")
    @JsonManagedReference // Gérer la sérialisation JSON
    private Zone zone;
    
}