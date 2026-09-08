package com.wakana.realestateworks.services.impl;

import com.wakana.realestateworks.enums.SubscriptionPlanEnum;
import com.wakana.realestateworks.model.SubscriptionPlan;
import com.wakana.realestateworks.repository.SubscriptionPlanRepository;
import com.wakana.realestateworks.services.SubscriptionPlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
    }

    @Override
    public SubscriptionPlan createPlan(SubscriptionPlanEnum name, String label, String description,
            double totalCost, int installmentCount, int projectLimit,
            boolean unlimitedProjects, double yearlyDiscountRate) {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setName(name);
        plan.setLabel(label);
        plan.setDescription(description);
        plan.setTotalCost(totalCost);
        plan.setInstallmentCount(installmentCount);
        plan.setProjectLimit(projectLimit);
        plan.setUnlimitedProjects(unlimitedProjects);
        plan.setYearlyDiscountRate(yearlyDiscountRate);
        return subscriptionPlanRepository.save(plan);
    }

    @Override
    public SubscriptionPlan getPlanById(Long id) {
        return subscriptionPlanRepository.findById(id).orElse(null);
    }

    @Override
    public List<SubscriptionPlan> getPlansByName(SubscriptionPlanEnum name) {
        return subscriptionPlanRepository.findAllByName(name);
    }

    @Override
    public List<SubscriptionPlan> getAllPlans() {
        return subscriptionPlanRepository.findAll();
    }

    @Override
    public void updatePlan(Long id, String label, String description, double totalCost, int installmentCount,
            int projectLimit, boolean unlimitedProjects, double yearlyDiscountRate) {

        subscriptionPlanRepository.findById(id).ifPresent(plan -> {
            plan.setLabel(label);
            plan.setDescription(description);
            plan.setTotalCost(totalCost);
            plan.setInstallmentCount(installmentCount);
            plan.setProjectLimit(projectLimit);
            plan.setUnlimitedProjects(unlimitedProjects);
            plan.setYearlyDiscountRate(yearlyDiscountRate);
            subscriptionPlanRepository.save(plan);
        });
    }

    @Override
    public void deletePlan(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }
}
