package com.wakana.samater.services;
import java.util.List;
import com.wakana.samater.dto.FeedBackRequest;
import com.wakana.samater.dto.GareRequest;
import com.wakana.samater.model.FeedBack;
import com.wakana.samater.model.Gare;


public interface GareService {
     List<  Gare> getAllGars() ;
   Gare saveGare(GareRequest gareRequest ) ;
    // List<Categorie> findCategorieByLibelle(String libelle);
    
}
