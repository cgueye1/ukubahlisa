package com.wakana.samater.model;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "categorie_mot" )

public class Categorie {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private Long idUser;
   /*  @OneToMany(mappedBy = "categorie")
    private List<Expression> expressions;*/
// cascade = CascadeType.ALL, orphanRemoval = true

    
}
