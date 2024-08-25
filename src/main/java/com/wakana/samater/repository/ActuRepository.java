package com.wakana.samater.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.Actu;

public interface ActuRepository  extends JpaRepository< Actu , Long> {

       Page<Actu> findAllByOrderByDateDesc(Pageable pageable);
       Page<Actu> findByTitleContainingOrDescriptionContainingOrderByDateDesc(String title, String description, Pageable pageable);

}
