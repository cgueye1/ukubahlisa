package com.wakana.samater.services;
import java.util.List;
import com.wakana.samater.dto.TicketRequest;
import com.wakana.samater.model.Ticket;


public interface TicketService {
     List< Ticket> getAllTicket() ;
     Ticket saveTicket(TicketRequest actuRequest ) ;
     Ticket check(Long id) ;
     Ticket getById(Long id) ;
     List< Ticket> getAllTicketByUser(Long idUser) ;
     
    
    // List<Categorie> findCategorieByLibelle(String libelle);
    
}
