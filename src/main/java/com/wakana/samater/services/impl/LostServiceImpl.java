package com.wakana.samater.services.impl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.wakana.samater.dto.LostRequest;
import com.wakana.samater.model.LostThing;
import com.wakana.samater.model.Stop; 
import com.wakana.samater.repository.LostThingRepository;
import com.wakana.samater.repository.StopRepository; 
import com.wakana.samater.services.LostService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostServiceImpl implements LostService {
    private final LostThingRepository lostThingRepository;
    private final StopRepository stopRepository; 

    @Override
    public LostThing saveLost(LostRequest lostRequest) {
        LostThing lostThing = new LostThing();
        lostThing.setFirstname(lostRequest.getFirstname());
        lostThing.setLastname(lostRequest.getLastname());
        lostThing.setPhonenumber(lostRequest.getCall());
        lostThing.setDate(lostRequest.getDate());
        lostThing.setTime(lostRequest.getTime());
        lostThing.setDescription(lostRequest.getDescription());
        lostThing.setOther(lostRequest.getOther());
        lostThing.setImg(lostRequest.getImg());
        
        
        Optional<Stop> optionalFromStop = stopRepository.findById(lostRequest.getFromStopId());
        if (optionalFromStop.isEmpty()) {
            return null; 
        }
        Stop fromStop  = optionalFromStop.get();
        lostThing.setFromStop(fromStop);
        
        Optional<Stop> optionalToStop = stopRepository.findById(lostRequest.getToStopId());
        if (optionalToStop .isEmpty()) {
            return null; 
        }
        Stop toStop  = optionalToStop .get();
       
        lostThing.setToStop(toStop);
        
     

        return lostThingRepository.save(lostThing);
    }

    @Override
    public LostThing getLost(Long id) {
        return lostThingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LostThing not found with id: " + id));
    }

    @Override
    public LostThing founded(Long id) {
        LostThing lostThing = lostThingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LostThing not found with id: " + id));
        
        lostThing.setFounded(!lostThing.isFounded()); 
        return lostThingRepository.save(lostThing);
    }

    @Override
    public Page<LostThing> findAllByOrderByDateDesc(Pageable pageable) {
        return lostThingRepository.findAllByOrderByDateDesc(pageable);
    }

    @Override
    public Page<LostThing> findByFirstnameContainingOrLastnameContainingOrDescriptionContainingOrderByDateDesc(
            String firstname, String lastname, String description, Pageable pageable) {
        return lostThingRepository.findByFirstnameContainingOrLastnameContainingOrDescriptionContainingOrderByDateDesc(
                firstname, lastname, description, pageable);
    }
}
