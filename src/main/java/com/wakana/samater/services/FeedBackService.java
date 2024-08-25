package com.wakana.samater.services;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.samater.dto.FeedBackRequest;
import com.wakana.samater.model.FeedBack;

public interface FeedBackService {
     List<  FeedBack> getAllFeedBacks() ;
     FeedBack saveFeedBack(FeedBackRequest feedBackRequest ) ;
    Page<FeedBack> findAllByOrderByDateDesc(Pageable pageable);
    Page<FeedBack>  findByTitleContainingOrMessageContainingOrderByDateDesc(String title, String message, Pageable pageable);
    
}
