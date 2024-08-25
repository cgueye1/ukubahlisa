package com.wakana.samater.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.ZoneIntervalle;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ZoneIntervalleRepository extends JpaRepository<ZoneIntervalle, Long> {
    @Query("SELECT zi FROM ZoneIntervalle zi ORDER BY CASE WHEN zi.m_fin_de_zone = false THEN 0 ELSE 1 END, zi.zone.libelle")
    List<ZoneIntervalle> findAllOrderedByZoneLibelleAndMFinDeZone();
}


