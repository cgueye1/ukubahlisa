package com.wakana.realestateworks.controller;

import com.wakana.realestateworks.enums.SubscriptionPlanEnum;
import com.wakana.realestateworks.model.SubscriptionPlan;
import com.wakana.realestateworks.services.SubscriptionPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @PostMapping
    public SubscriptionPlan createPlan(@RequestBody SubscriptionPlan plan) {
        return subscriptionPlanService.createPlan(
                plan.getName(),
                plan.getLabel(),
                plan.getDescription(),
                plan.getTotalCost(),
                plan.getInstallmentCount(),
                plan.getProjectLimit(),
                plan.isUnlimitedProjects(),
                plan.getYearlyDiscountRate());
    }

    @GetMapping
    public List<SubscriptionPlan> getAllPlans() {
        return subscriptionPlanService.getAllPlans();
    }

    @GetMapping("/{id}")
    public SubscriptionPlan getPlanById(@PathVariable Long id) {
        return subscriptionPlanService.getPlanById(id);
    }

    @GetMapping("/name/{name}")
    public List<SubscriptionPlan> getPlansByName(@PathVariable SubscriptionPlanEnum name) {
        return subscriptionPlanService.getPlansByName(name);
    }

    @PutMapping("/{id}")
    public void updatePlan(@PathVariable Long id, @RequestBody SubscriptionPlan plan) {
        subscriptionPlanService.updatePlan(
                id,
                plan.getLabel(),
                plan.getDescription(),
                plan.getTotalCost(),
                plan.getInstallmentCount(),
                plan.getProjectLimit(),
                plan.isUnlimitedProjects(),
                plan.getYearlyDiscountRate());
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable Long id) {
        subscriptionPlanService.deletePlan(id);
    }
}
