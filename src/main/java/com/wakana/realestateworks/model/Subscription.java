package com.wakana.realestateworks.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wakana.realestateworks.enums.PaymentCallStatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "subscriptions2")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_plan1_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private double paidAmount;
    private int installmentCount;
    private LocalDate dateInvoice;

    @Enumerated(EnumType.STRING)
    private PaymentCallStatusEnum status;

    private boolean renewed = false;
    private int currentProjectCount = 0; // combien de projets actifs liés
    private int remainingProjects; // calculé automatiquement

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<RealEstateProperty> properties = new ArrayList<>();

    public boolean canCreateNewProject() {
        return subscriptionPlan.isUnlimitedProjects()
                || currentProjectCount < subscriptionPlan.getProjectLimit();
    }

    public void incrementProjects() {
        if (!subscriptionPlan.isUnlimitedProjects()) {
            this.currentProjectCount++;
            this.remainingProjects = subscriptionPlan.getProjectLimit() - this.currentProjectCount;
        }
    }

    public void decrementProjects() {
        if (!subscriptionPlan.isUnlimitedProjects() && this.currentProjectCount > 0) {
            this.currentProjectCount--;
            this.remainingProjects = subscriptionPlan.getProjectLimit() - this.currentProjectCount;
        }
    }
}
