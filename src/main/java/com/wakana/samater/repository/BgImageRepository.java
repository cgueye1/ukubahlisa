package com.wakana.samater.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.BgImage;

public interface BgImageRepository  extends JpaRepository<BgImage , Long> {
       
       
       BgImage findFirstByOrderByDateDesc(); 

      // Page<Bgimag> findAllByOrderByDateDesc(Pageable pageable);
      // Page<Bgimag> findByTitleContainingOrDescriptionContainingOrderByDateDesc(String title, String description, Pageable pageable);

}
