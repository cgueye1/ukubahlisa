package com.wakana.realestateworks.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wakana.realestateworks.dto.response.MonthlyStatsDTO;
import com.wakana.realestateworks.dto.response.SubscriptionKpiDto;
import com.wakana.realestateworks.dto.response.SubscriptionPlanPercentageDTO;
import com.wakana.realestateworks.enums.PaymentCallStatusEnum;
import com.wakana.realestateworks.model.Invoice;
import com.wakana.realestateworks.model.Subscription;
import com.wakana.realestateworks.model.User;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionService {

    Subscription createSubscription(User user, Long planId, int months);

    Subscription renewSubscription(Long subscriptionId, int months);

    boolean isSubscriptionActive(Long userId);

    boolean canCreateProject(Long userId);

    void onProjectCreated(Long userId);

    void onProjectDeleted(Long userId);

    Subscription payInstallment(Long subscriptionId, double amount);

    List<Subscription> getAllSubscriptions();

    Page<Subscription> getUserSubscriptionsOrderedByEndDate(Long userId, Pageable pageable);

    SubscriptionKpiDto getSubscriptionKpis();

    Subscription getSubscriptionByUserId(Long userId);

    List<MonthlyStatsDTO> getSubscriptionEvolution(Integer year);

    List<SubscriptionPlanPercentageDTO> getSubscriptionPlanDistribution();

  
}
