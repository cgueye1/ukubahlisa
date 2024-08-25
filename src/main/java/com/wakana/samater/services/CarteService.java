package com.wakana.samater.services;
import java.util.List;
import com.wakana.samater.dto.CarteRequest;
import com.wakana.samater.model.Carte;
import com.wakana.samater.model.CarteHistory;
import com.wakana.samater.dto.ResponseCust;



public interface CarteService {
     List< Carte> getAllCartes() ;

     Carte saveCarte(CarteRequest carteRequest ) ;
     ResponseCust check(Long id) ;
     Carte abonnement(CarteRequest carteRequest,Long id) ;
     Carte getById(Long id) ;
     List< Carte> getAllCarteByUser(Long idUser) ;
     List< CarteHistory> getAllCarteHistoriesByUser(Long idUser) ;
         
}
