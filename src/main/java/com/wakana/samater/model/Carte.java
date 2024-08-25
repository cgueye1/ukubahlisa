package com.wakana.samater.model;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wakana.samater.dto.CarteRequest;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carte")
public class Carte { 
                    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String numero;

    private TypeAbonnement type_abonnement;

    private String date_last_abonnement;
   
    private String date_fin_validite;
 
    private String qrcode;
   
    private int nombre_voyage;

    private int classe;
    @ManyToOne
    @JoinColumn(name = "zone_intervalle_id", referencedColumnName = "id")
    @JsonManagedReference // Gérer la sérialisation JSON
    private ZoneIntervalle zoneintervalle;
    
    
    
public static Carte fromCarteRequest(CarteRequest carterRequest) {
// Obtention de l'heure actuelle de Dakar
ZonedDateTime debut = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
// Ajouter un mois à la date de début pour obtenir la date de fin
ZonedDateTime fin =carterRequest.getType_abonnement()=="mensuel" ?debut.plusMonths(1):debut.plusWeeks(1);
// Formater la date et l'heure
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
String formattedDebut = debut.format(formatter);
String formattedFin = fin.format(formatter);
Carte carte = new Carte();
carte.setDate_last_abonnement(formattedDebut);
carte.setDate_fin_validite(carterRequest.getType_abonnement()!="hebdomadaire"?formattedFin:"");
carte.setNombre_voyage(0);
carte.setType_abonnement(carterRequest.getType_abonnement()=="mensuel"?TypeAbonnement.MENSUEL: (carterRequest.getType_abonnement()=="hebdomadaire"?TypeAbonnement.HEBDOMADAIRE:TypeAbonnement.FORFAIT));


   return carte;
}

   
}
