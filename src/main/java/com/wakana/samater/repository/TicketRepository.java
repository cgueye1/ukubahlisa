package com.wakana.samater.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.Ticket;
import java.util.List;


public interface TicketRepository  extends JpaRepository< Ticket , Long> {
    List<Ticket> findByIdUser(Long idUser);
    
}
