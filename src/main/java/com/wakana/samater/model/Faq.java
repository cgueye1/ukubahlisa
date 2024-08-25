package com.wakana.samater.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "faq")
public class Faq {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String title;
    private Long timestamp;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;  
    
    

   
}
