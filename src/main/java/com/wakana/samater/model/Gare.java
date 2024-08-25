package com.wakana.samater.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gare")
public class Gare {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private String img;
    private double latitude;
    private double longitude;
    @ManyToOne
    @JoinColumn(name = "zone_id", referencedColumnName = "id")
    private Zone zone;
    
    
   


   
}
