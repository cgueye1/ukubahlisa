package com.wakana.samater.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vod")
public class Vod {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String title;
    private String link;


   
}
