package com.wakana.samater.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.Stop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StopRepository  extends JpaRepository< Stop , String> {

    Page<Stop> findAllByOrderByStopCodeAsc(Pageable pageable);
    Page<Stop> findByStopNameContainingOrderByStopCodeAsc(String keyword, Pageable pageable);

}
