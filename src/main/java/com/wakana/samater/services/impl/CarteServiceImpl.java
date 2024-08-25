package com.wakana.samater.services.impl;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.CarteRequest;
import com.wakana.samater.dto.ResponseCust;
import com.wakana.samater.model.Carte;
import com.wakana.samater.model.CarteHistory;
import com.wakana.samater.model.TypeAbonnement;
import com.wakana.samater.model.ZoneIntervalle;
import com.wakana.samater.repository.CarteHistoryRepository;
import com.wakana.samater.repository.CarteRepository;
import com.wakana.samater.repository.ZoneIntervalleRepository;
import com.wakana.samater.services.CarteService;
import com.wakana.samater.util.QRCodeUtil;
import lombok.RequiredArgsConstructor;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CarteServiceImpl implements CarteService{  

    private final CarteRepository carteRepository;
    private final CarteHistoryRepository carteHistoryRepository;
    private final ZoneIntervalleRepository zoneIntervalleRepository;



    public Carte saveCarte(CarteRequest carteRequest) {
      Carte carte =new Carte();
      carte.setUserId(carteRequest.getId_user());
      carte.setNumero(carteRequest.getNumero());
       Carte savedCarte = carteRepository.save(carte);

      savedCarte .setQrcode(QRCodeUtil.generateQrCode(String.format("%04d",savedCarte.getUserId())));
      //String.format("%04d", savedCarte.getId())
      return carteRepository.save( savedCarte );
    }
  

   


    public List<Carte> getAllCartes() {
     return carteRepository.findAll();
    }




    public List<Carte> getAllCarteByUser(Long idUser) {
        return carteRepository.findByUserId(idUser);
    }

    public  ResponseCust  check(Long id) {
      boolean success = false;
      String message = "";

      Optional<Carte> optionalCarte = carteRepository.findById(id);
      if (optionalCarte.isPresent()) {
          Carte carte = optionalCarte.get();
          //forfait
          if (carte.getType_abonnement() == TypeAbonnement.FORFAIT) {
              if (carte.getNombre_voyage() > 0) {
                  carte.setNombre_voyage(carte.getNombre_voyage() - 1);
                  carteRepository.save(carte);
                  success = true;
                  message = "Utilisation du voyage avec succès.";
              } else {
                  message = "Le nombre de voyages est insuffisant.";
              }
          } else {
              ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
              ZonedDateTime dateFin = ZonedDateTime.parse(carte.getDate_fin_validite(), formatter);
  
              if (now.isBefore(dateFin)) {
                  success = true;
                  message = "La carte est encore valide.";
              } else if (now.isAfter(dateFin)) {
                  message = "La carte a expiré.";
              } else {
                  success = true;
                  message = "La carte expire maintenant.";
              }
          }
      } else {
          message = "Carte non trouvée.";
      }

      return new ResponseCust (success, message);
  }

    public Carte getById(Long id) {
      return carteRepository.findById(id).orElse(null);
    }





    @Override
    public  Carte abonnement(CarteRequest carteRequest, Long id) {
        
        Carte carte = carteRepository.findById(id).orElse(null);
        ZoneIntervalle zoneIntervalle = zoneIntervalleRepository.findById(carteRequest.getId_zone_inter()).orElse(null);
        if(null==carte){
            return null;
            
        } 
   ZonedDateTime debut = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));
// Ajouter un mois à la date de début pour obtenir la date de fin
   ZonedDateTime fin =carteRequest.getType_abonnement().equals("mensuel") ?debut.plusMonths(1):debut.plusWeeks(1);
// Formater la date et l'heure
   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
   String formattedDebut = debut.format(formatter);
   String formattedFin = fin.format(formatter);
   carte.setDate_last_abonnement(formattedDebut);
   carte.setZoneintervalle(zoneIntervalle);
   carte.setClasse(carteRequest.getClasse());
   carte.setDate_fin_validite(formattedFin);
   carte.setNombre_voyage(carteRequest.getNombre_voyage());
   carte.setType_abonnement(carteRequest.getType_abonnement().equals("mensuel")?TypeAbonnement.MENSUEL: (carteRequest.getType_abonnement() .equals("hebdomadaire")?TypeAbonnement.HEBDOMADAIRE:TypeAbonnement.FORFAIT));
   
   //save history
   CarteHistory carteHistory =  CarteHistory.fromCarteRequest(carteRequest) ;
   carteHistory.setDate_fin_validite(formattedFin );
   carteHistory.setDate_last_abonnement(formattedDebut);
   carteHistory.setZoneintervalle(zoneIntervalle);
   carteHistory.setNumero(carte.getNumero());
   carteHistory.setNombre_voyage(carte.getNombre_voyage());
   carteHistoryRepository.save(carteHistory);

   return carteRepository.save(carte);
     
    }






    public List<CarteHistory> getAllCarteHistoriesByUser(Long idUser) {
       return carteHistoryRepository.findByUserId(idUser);
    }





  

  


    
     
     
    
}
