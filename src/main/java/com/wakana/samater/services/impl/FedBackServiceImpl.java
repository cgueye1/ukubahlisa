package com.wakana.samater.services.impl;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.wakana.samater.dto.FeedBackRequest;
import com.wakana.samater.model.FeedBack;
import com.wakana.samater.repository.FeedBackRepository;
import com.wakana.samater.repository.UserRepository;
import com.wakana.samater.services.FeedBackService;
import com.wakana.samater.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FedBackServiceImpl implements FeedBackService{  
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
 

    @Override
    public List<  FeedBack> getAllFeedBacks() {
        return feedBackRepository.findAll();
    }
    @Override
    public   FeedBack saveFeedBack(FeedBackRequest feedBackRequest) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Dakar"));

         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
         String formattedDate = now.format(formatter);
         long timestamp =  now .toEpochSecond();
         Optional<User> optionalUser = userRepository.findById(feedBackRequest.getIdAuteur());
        if (optionalUser.isEmpty()) {
            return null; 
        }
        User user = optionalUser.get();
        
          FeedBack feedBack = new  FeedBack ();
          feedBack.setUser(user);
          feedBack.setDate(formattedDate);
          feedBack.setMessage(feedBackRequest.getMessage());
          feedBack.setNote(feedBackRequest.getNote());
          feedBack.setTitle(feedBackRequest.getTitre());
          return feedBackRepository.save(feedBack);
        

    }
    @Override
    public Page<FeedBack> findAllByOrderByDateDesc(Pageable pageable) {
        
        return feedBackRepository.findAllByOrderByDateDesc(pageable);
    }
    @Override
    public Page<FeedBack> findByTitleContainingOrMessageContainingOrderByDateDesc(String title, String message,
            Pageable pageable) {
     
     
     return feedBackRepository.findByTitleContainingOrMessageContainingOrderByDateDesc(title,message,pageable);
    }


    
     
     
    
}
