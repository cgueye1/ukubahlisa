package com.wakana.samater.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.FeedBack;

public interface FeedBackRepository  extends JpaRepository< FeedBack , Long> {
    Page<FeedBack> findAllByOrderByDateDesc(Pageable pageable);
    Page<FeedBack>  findByTitleContainingOrMessageContainingOrderByDateDesc(String title, String message, Pageable pageable);
    
}
