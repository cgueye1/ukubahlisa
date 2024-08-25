package com.wakana.samater.services;
import java.util.List;

import com.wakana.samater.model.TerNotifications;
public interface TerNotificationService {
  
     TerNotifications saveNotification(TerNotifications terNotifications ) ;
     List<TerNotifications> findByidUser(Long idUser);
     TerNotifications view(Long id);

    
}
