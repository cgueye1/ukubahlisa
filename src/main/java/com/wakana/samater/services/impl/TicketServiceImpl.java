package com.wakana.samater.services.impl;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.TicketRequest;
import com.wakana.samater.model.TerNotifications;
import com.wakana.samater.model.Ticket;
import com.wakana.samater.model.TypeDeVoyage;
import com.wakana.samater.model.ZoneIntervalle;
import com.wakana.samater.repository.TicketRepository;
import com.wakana.samater.repository.ZoneIntervalleRepository;
import com.wakana.samater.services.TerNotificationService;
import com.wakana.samater.services.TicketService;
import com.wakana.samater.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{  
    private final TicketRepository ticketRepository;
    private final ZoneIntervalleRepository zoneRepository;
    private final TerNotificationService terNotificationService;
    public List<Ticket> getAllTicket() {
      return ticketRepository.findAll();
    }

    @Override
    public Ticket saveTicket(TicketRequest ticketRequest) {
        // Obtention de l'heure actuelle de Dakar
     ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
    // Formater la date et l'heure
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
     String formattedDateTime = now.format(formatter);
     Ticket ticket =Ticket.fromTicketRequest(ticketRequest);
     ZoneIntervalle zone =  zoneRepository.findById(ticketRequest.getIdZoneInter()).orElse(null);
     Ticket savedTicket = ticketRepository.save(ticket);
     savedTicket.setNum_serie(String.format("%04d", savedTicket.getId()));
     savedTicket.setQrcode(QRCodeUtil.generateQrCode(String.format("%04d", savedTicket.getId())));
     savedTicket.setZoneIntervalle(zone);
     ///
     TerNotifications terNotifications =new TerNotifications();
     terNotifications.setDescription(ticket.getProduit());
     terNotifications.setTitle("Achet de ticket");
     terNotifications.setIdUser(ticket.getIdUser());
     terNotifications.setDate(formattedDateTime );
     terNotificationService.saveNotification(terNotifications);
     return ticketRepository.save(savedTicket );
     
    }

    @Override
    public List<Ticket> getAllTicketByUser(Long idUser) {
      return ticketRepository.findByIdUser(idUser);
    }

    @Override
    public Ticket check(Long id) {
        // Obtention de l'heure actuelle de Dakar
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
        // Formater la date et l'heure
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
         String formattedDateTime = now.format(formatter);
         Ticket ticket =  ticketRepository.findById(id).orElse(null);
       
         ticket.setScanNumer( ticket.getScanNumer()+1);
         ticket.setChecked_date(formattedDateTime);
         if(ticket.getType_de_voyage()==TypeDeVoyage.ALLER_RETOUR & ticket.getScanNumer()==2){
           ticket.set_history(true);
         } else if(ticket.getScanNumer()==1){
           ticket.set_history(true);
         }
    
         return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getById(Long id) {
      return ticketRepository.findById(id).orElse(null);
    }

  

  


    
     
     
    
}
