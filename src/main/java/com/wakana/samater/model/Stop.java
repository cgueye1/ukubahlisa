package com.wakana.samater.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Entity
@Table(name = "stop")
public class Stop {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "location_type")
    private String locationType;

   
    private boolean lostAndFound;

    @Column(name = "parent_station")
    private String parentStation;

  
    private boolean parking;

    @Column(name = "picture")
    private String picture;

    @Column(name = "platform_code")
    private String platformCode;

 
    private boolean shops;

    @JsonProperty("stop_code")
    private String stopCode;

    @JsonProperty("stop_desc")
    private String stopDesc;

    @Column(name = "stop_lat")
    private double stopLat;

    @Column(name = "stop_lon")
    private double stopLon;

   @JsonProperty("stop_name")
    private String stopName;


    private String stopTimezone;


    private boolean terAgency;


    private boolean toilets;

   
    private boolean waitingRoom;

    @ElementCollection
    @Column(name = "pictures")
    private List<String> pictures;
    
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "stop_shop",
        joinColumns = @JoinColumn(name = "stop_id"),
        inverseJoinColumns = @JoinColumn(name = "shop_id")
    )
    private List<Shop> shopslist;
    
    
    
}
