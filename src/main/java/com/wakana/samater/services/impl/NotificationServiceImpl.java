package com.wakana.samater.services.impl;
import java.util.List;
import org.springframework.stereotype.Service;
import com.wakana.samater.model.TerNotifications;
import com.wakana.samater.repository.NotificationRepository;
import com.wakana.samater.services.TerNotificationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements TerNotificationService{  
    private final NotificationRepository notificationRepository;
    

    public TerNotifications saveNotification(TerNotifications terNotifications) {
      
        return notificationRepository.save(terNotifications);
    }

 

    public List<TerNotifications> findByidUser(Long idUser) {
      
        return notificationRepository.findByidUserOrderByDateDesc(idUser);
     
    }


    public TerNotifications view(Long id) {
      TerNotifications notif = notificationRepository.findById(id).orElse(null);
      notif.setViewed(true);
      return  notificationRepository.save(notif);
     
    }


  


    
     
     
    
}
