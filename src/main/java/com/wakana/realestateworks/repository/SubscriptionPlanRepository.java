package com.wakana.realestateworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wakana.realestateworks.enums.SubscriptionPlanEnum;
import com.wakana.realestateworks.model.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    // Optional<SubscriptionPlan> findByName(SubscriptionPlanEnum name);
    List<SubscriptionPlan> findAllByName(SubscriptionPlanEnum name);

}
