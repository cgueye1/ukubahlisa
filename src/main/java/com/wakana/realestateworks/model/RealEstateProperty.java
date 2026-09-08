package com.wakana.realestateworks.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wakana.realestateworks.dto.response.RealEstateResponseDto;
import com.wakana.realestateworks.enums.ConstructionStatusEnum;
import com.wakana.realestateworks.enums.RealEstatePropertyStatusEnum;

import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "companies")
public class RealEstateProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // Name of the property
    private String number; // Property number
    private String address; // Property address
    private double price; // Property price
    private int numberOfRooms; // Number of rooms
    private double area; // Area in square meters
    private String latitude; // Latitude
    private String longitude; // Longitude
    private boolean available; // Availability status
    private double reservationFee; // Reservation fee
    private double discount; // remise
    private double feesFile;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description; // Long text description
    private String plan; // Plan (e.g., building plans)
    private String legalStatus; // Legal status (Real rights status)
    private int numberOfLots; // Number of lots in the property
    private int level; // The level (floor) of the apartment
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promoter_id", nullable = true)
    private User promoter; // Promoter of the property
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_recipient_id", nullable = true)
    private User recipient; // Recipient of the property

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moa_id", nullable = true)
    private User moa; // moa of the property

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = true)
    private User manager; // manager of the property

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notary_id", nullable = true)
    private User notary; // Notary of the property
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = true)
    private User agency; // Notary of the property
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = true)
    private User bank; // Notary of the property
    // Self-referencing relationship for sub-properties (e.g., apartments/lots in a
    // building)
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_property_id")
    private RealEstateProperty parentProperty; // Parent property (e.g., the building)

    @JsonIgnore
    @OneToMany(mappedBy = "parentProperty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RealEstateProperty> subProperties; // Sub-properties (e.g., apartments/lots in the building)
    private Long timestamp;
    @ElementCollection
    @Column(name = "pictures")
    private List<String> pictures;
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_type_id", nullable = false)
    private PropertyType propertyType;

   

    @Enumerated(EnumType.STRING)
    private RealEstatePropertyStatusEnum status;

    @Enumerated(EnumType.STRING)
    private ConstructionStatusEnum constructionStatus;

    private boolean lotFeesPaid;

 
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "property_user_mapping", joinColumns = @JoinColumn(name = "property_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> owners;
    private double budget;
    private LocalDateTime allocateDate;
  
    private int commentcount;

    private LocalDateTime soldAt;

   

    @JsonIgnore
    @OneToMany(mappedBy = "realEstateProperty", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Task> tasks;

    @OneToMany(mappedBy = "assignedCompany", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<User> workers;

    private boolean isMezzanine;

    // Project start date
    private LocalDateTime startDate;

    // Project end date

    @ManyToOne
    @JoinColumn(name = "subscription2_id")
    private Subscription subscription;

    private LocalDateTime endDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "realEstateProperty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointingAddress> pointingAddresses;

    public RealEstateResponseDto convertToRealEstateDto() {
        return new RealEstateResponseDto(
                this.getId(),
                this.getName());
    }

}
