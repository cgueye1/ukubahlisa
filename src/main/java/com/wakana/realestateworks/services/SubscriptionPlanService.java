package com.wakana.realestateworks.services;

import java.util.List;

import com.wakana.realestateworks.enums.SubscriptionPlanEnum;
import com.wakana.realestateworks.model.SubscriptionPlan;

public interface SubscriptionPlanService {
    
    SubscriptionPlan createPlan(
        SubscriptionPlanEnum name,
        String label,
        String description,
        double totalCost,
        int installmentCount,
        int projectLimit,
        boolean unlimitedProjects,
        double yearlyDiscountRate
    );

    SubscriptionPlan getPlanById(Long id);

    List<SubscriptionPlan> getPlansByName(SubscriptionPlanEnum name);

    List<SubscriptionPlan> getAllPlans();

    void updatePlan(
        Long id,
        String label,
        String description,
        double totalCost,
        int installmentCount,
        int projectLimit,
        boolean unlimitedProjects,
        double yearlyDiscountRate
    );

    void deletePlan(Long id);
}
