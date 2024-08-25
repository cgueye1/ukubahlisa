package com.wakana.samater.model;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wakana.samater.dto.TicketRequest;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {      

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String num_serie;
    private Long idUser;
    private Double duree;
    private String qrcode;
    private String date_emission;
    private String fin_validite;
    private String produit;
    private String mode_paiement;
    private String reference;
    private  TypeDeVoyage type_de_voyage;
    private int scanNumer;
    private int classe;
    private String checked_date;
    private boolean is_history;
    @ManyToOne
    @JoinColumn(name = "zoneintervalle_id")
    private ZoneIntervalle zoneIntervalle;
    
public static Ticket fromTicketRequest(TicketRequest ticketRequest) {
    // Obtention de l'heure actuelle de Dakar
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
    // Formater la date et l'heure
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDateTime = now.format(formatter);
    
    
    // Calcul de la date et l'heure de validité cinq jours après
    ZonedDateTime dateHeureFinValidite = now.plusDays(5); 
    // Formater la date et l'heure de validité
    String dateHeureFinValiditeFormatted = dateHeureFinValidite.format(formatter);
    System.out.println("Date et heure de fin de validité : " + dateHeureFinValiditeFormatted);
   
   Ticket ticket = new Ticket();
   ticket.setIdUser(ticketRequest.getIdUser());
   ticket.setQrcode("" );
   ticket.setDate_emission(formattedDateTime);
   ticket.setFin_validite(dateHeureFinValiditeFormatted);
   ticket.setProduit(ticketRequest.getProduit());
   ticket.setMode_paiement(ticketRequest.getMode_paiement());
   ticket.setReference(ticketRequest.getReference());
   ticket.setType_de_voyage(ticketRequest.isAllersimple()? TypeDeVoyage.ALLER_SIMPLE: TypeDeVoyage.ALLER_RETOUR);
   ticket.setScanNumer(0);
   ticket.setChecked_date(null);
   ticket.set_history(false);
   ticket.setDuree(ticketRequest.getDuree());
   ticket.setClasse(ticketRequest.getClasse());
   return ticket;
}


   
}
