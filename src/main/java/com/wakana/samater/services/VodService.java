package com.wakana.samater.services;
import java.util.List;
import com.wakana.samater.dto.FeedBackRequest;
import com.wakana.samater.dto.VodRequest;
import com.wakana.samater.model.FeedBack;
import com.wakana.samater.model.Vod;

public interface VodService {
     List<  Vod> getAllVideos() ;
     Vod saveVideo(VodRequest vodRequest ) ;
    // List<Categorie> findCategorieByLibelle(String libelle);
    
}
