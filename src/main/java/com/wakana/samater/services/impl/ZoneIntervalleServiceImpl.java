package com.wakana.samater.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wakana.samater.dto.ZoneIntervalleRequest;
import com.wakana.samater.model.Gare;
import com.wakana.samater.model.Zone;
import com.wakana.samater.model.ZoneIntervalle;
import com.wakana.samater.repository.GareRepository;
import com.wakana.samater.repository.ZoneIntervalleRepository;
import com.wakana.samater.repository.ZoneRepository;
import com.wakana.samater.services.ZoneIntervalleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZoneIntervalleServiceImpl implements ZoneIntervalleService {

    private final ZoneIntervalleRepository zoneIntervalleRepository;
    private final GareRepository gareRepository;
    private final ZoneRepository zoneRepository;

    @Override
    public List<ZoneIntervalle> getAllZoneIntervalle() {
        return zoneIntervalleRepository.findAllOrderedByZoneLibelleAndMFinDeZone();
    }

    @Override
    public ZoneIntervalle saveZoneIntervalle(ZoneIntervalleRequest zoneIntervalleRequest) {
        Gare gareDepart = gareRepository.findById(zoneIntervalleRequest.getGare_depart_id()).orElse(null);
        Gare gareArrivee = gareRepository.findById(zoneIntervalleRequest.getGare_arrivee_id()).orElse(null);
        Zone zone = zoneRepository.findById(zoneIntervalleRequest.getZone_id()).orElse(null);
    
        if (zone == null) {
            // Gérer le cas où la zone n'est pas trouvée
            // Peut-être lever une exception ou effectuer d'autres actions nécessaires
            return null;
        }
    
        ZoneIntervalle zoneI = new ZoneIntervalle();
        zoneI.setVoie(zoneIntervalleRequest.getVoie());
        zoneI.setDuree(zoneIntervalleRequest.getDuree());
        zoneI.setGareDepart(gareDepart);
        zoneI.setGareArrive(gareArrivee);
        zoneI.setM_fin_de_zone(zoneIntervalleRequest.isM_fin_de_zone());
    
        // Enregistrer le ZoneIntervalle
        ZoneIntervalle savedZoneI = zoneIntervalleRepository.save(zoneI);
        
        // Ajouter le ZoneIntervalle à la liste zoneIntervalles de la Zone
        zone.addZoneIntervalle(savedZoneI);

    
        // Enregistrer la zone mise à jour
        zoneRepository.save(zone);
    
        // Retourner le ZoneIntervalle enregistré
        return savedZoneI;
    }
    
}
