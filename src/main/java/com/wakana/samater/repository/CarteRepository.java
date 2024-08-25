package com.wakana.samater.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.Carte;
import java.util.List;


public interface CarteRepository  extends JpaRepository<Carte , Long> {
    List<Carte> findByUserId(Long userId);;
    
}
