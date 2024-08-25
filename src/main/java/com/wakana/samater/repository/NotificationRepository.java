package com.wakana.samater.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.samater.model.TerNotifications;

public interface NotificationRepository  extends JpaRepository< TerNotifications , Long> {

    List<TerNotifications> findByidUserOrderByDateDesc(Long idUser);
    
}
