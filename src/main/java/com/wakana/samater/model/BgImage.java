package com.wakana.samater.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bgimage")
public class BgImage {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String title;
    private String img;
    private String link;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;  
    

   
}
