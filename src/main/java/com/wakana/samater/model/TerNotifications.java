package com.wakana.samater.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notification")
public class TerNotifications {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private String date;
    private String title;
    private String img;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;  
    private boolean isCliquable;
    private boolean isViewed;
    

   
}
