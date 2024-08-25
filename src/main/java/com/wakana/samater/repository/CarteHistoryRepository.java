package com.wakana.samater.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.CarteHistory;

import java.util.List;


public interface CarteHistoryRepository  extends JpaRepository<CarteHistory , Long> {
    List<CarteHistory> findByUserId(Long userId);;
    
}
