package com.wakana.samater.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "shop")
@JsonIgnoreProperties(ignoreUnknown = true)

public class Shop {
     @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    @Column(columnDefinition = "LONGTEXT")
    private String descr;
    private String picture;
}
