package com.wakana.samater.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "voiceannoce")
public class VoiceAnnonce {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String title;
    private String audio;
    private Long timestamp;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String descr;   
    private boolean alert;
   
}
