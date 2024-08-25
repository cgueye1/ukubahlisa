package com.wakana.samater.model;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "zone")
public class Zone {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    
    private String libelle;
    private Double prix_ticket_first;
    private Double prix_ticket_second;
    private Double prix_ticket_finzone;
       //prix abonnement
       private Double prix_abo_hebdo_first;
       private Double prix_abo_hebdo_second;
       private Double prix_abo_mensuel_first;
       private Double prix_abo_mensuel_second;
       //
       private Double prix_abo_hebdo_10_22;
       private Double prix_abo_mensuel_10_22;
       private Double prix_abo_hebdo_moins_10;
       private Double prix_abo_mensuel_moins_10;
    
    
    
    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // Gérer la sérialisation JSON
    private List<ZoneIntervalle> zoneIntervalles ;

    // Méthode pour ajouter un ZoneIntervalle à la liste zoneIntervalles
    public void addZoneIntervalle(ZoneIntervalle zoneIntervalle) {
        if (zoneIntervalles == null) {
            zoneIntervalles = new ArrayList<>();
        }
        zoneIntervalles.add(zoneIntervalle);
        zoneIntervalle.setZone(this); // Met à jour la référence bidirectionnelle
    }
}