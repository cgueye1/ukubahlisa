package com.wakana.samater.services.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.ActuRequest;
import com.wakana.samater.model.Actu;
import com.wakana.samater.repository.ActuRepository;
import com.wakana.samater.services.ActuService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActuServiceImpl implements ActuService{  
    private final ActuRepository actuRepository;

    @Override
    public List<Actu> getAllActus() {
        
      return actuRepository.findAll();
      
    }

    @Override
    public Actu saveActu(ActuRequest actuRequest) {
       Actu actu = new Actu();
       actu.setDate(actuRequest.getDate());
       actu.setDescription(actuRequest.getDescription());
       actu.setImg(actuRequest.getImg());
       actu.setTitle(actuRequest.getTitle());
       actu.setLink(actuRequest.getLink());
       return actuRepository.save(actu);
    }

    @Override
    public Actu getActu(Long id) {
     return actuRepository.findById(id).orElse(null);
    }

    @Override
    public Actu updateActu(ActuRequest actuRequest, Long id) {
      
        Optional<Actu> optionalActu = actuRepository.findById(id);
        if (!optionalActu.isPresent()) {
            return null; 
        }
        Actu actu = optionalActu.get();
        actu.setDate(actuRequest.getDate());
        actu.setDescription(actuRequest.getDescription());
        actu.setTitle(actuRequest.getTitle());
        actu.setLink(actuRequest.getLink());
        if(actuRequest.getImg()!= null && !actuRequest.getImg().isEmpty()){
          actu.setImg(actuRequest.getImg());       
        }
        
        
        return actuRepository.save(actu);

  
    }

    @Override
    public Page<Actu> findAllByOrderByDateDesc(Pageable pageable) {
   
      return actuRepository.findAllByOrderByDateDesc(pageable);
    }

    @Override
    public Page<Actu> findByTitleContainingOrDescriptionContainingOrderByDateDesc(String title, String description,
        Pageable pageable) {
      
       return actuRepository.findByTitleContainingOrDescriptionContainingOrderByDateDesc(title,description,pageable);
    }

  

  


    
     
     
    
}
