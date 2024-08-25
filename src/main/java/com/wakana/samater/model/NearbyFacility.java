package com.wakana.samater.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wakana.samater.enums.NearbyFacilitiesEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "nearby_facility")
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class NearbyFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "descr")
    private String descr;

    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;

    @Column(name = "libelle")
    private String libelle;

    @Enumerated(EnumType.STRING)
    @Column(name = "nearby_facility_type")
    private NearbyFacilitiesEnum nearbyFacilityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_stop_id", referencedColumnName = "id")
    @JsonManagedReference 
    private Stop linkedStop;
}
