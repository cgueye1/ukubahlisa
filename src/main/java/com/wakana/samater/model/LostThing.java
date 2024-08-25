package com.wakana.samater.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "lost") 
public class LostThing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date; 
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String firstname;
    private String lastname;
    private boolean founded;
    private String img;
    private String phonenumber;
    private String time;
    private String link;
    private String other;
    
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_stop_id")
    private Stop fromStop;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_stop_id")
    private Stop toStop;

}
