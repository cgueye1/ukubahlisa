package com.wakana.realestateworks.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wakana.realestateworks.enums.SubscriptionPlanEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subscription_plan2")

public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPlanEnum name;

    @Column(nullable = false)
    private String label; // Ex: "BASIC", "PREMIUM"

    @Column(length = 500)
    private String description; // Ex: "Offre de base pour promoteurs", etc.

    private double totalCost;
    private int installmentCount;

    private int projectLimit; // Exemple : 3 pour BASIC, 0 pour illimité
    private boolean unlimitedProjects;

    private double yearlyDiscountRate; // ex: 0.15 pour 15% si abonnement annuel
    private boolean active = true;
    @JsonIgnore
    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions;
}
