package com.wakana.realestateworks.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.wakana.realestateworks.dto.response.MonthlyStatsDTO;
import com.wakana.realestateworks.dto.response.SubscriptionKpiDto;
import com.wakana.realestateworks.dto.response.SubscriptionPlanPercentageDTO;
import com.wakana.realestateworks.enums.PaymentCallStatusEnum;
import com.wakana.realestateworks.enums.SubscriptionPlanEnum;
import com.wakana.realestateworks.model.Invoice;
import com.wakana.realestateworks.model.Subscription;
import com.wakana.realestateworks.model.SubscriptionPlan;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.repository.InvoiceRepository;
import com.wakana.realestateworks.repository.SubscriptionPlanRepository;
import com.wakana.realestateworks.repository.SubscriptionRepository;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.services.InvoiceService;
import com.wakana.realestateworks.services.SubscriptionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

@Service
@Transactional
@RequiredArgsConstructor

public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserRepository userRepository;
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    @Override
    public Subscription createSubscription(User user, Long planId, int months) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        double totalCost = plan.getTotalCost();
        if (months >= 12 && plan.getYearlyDiscountRate() > 0)
            totalCost = totalCost * (1 - plan.getYearlyDiscountRate());

        // Vérifie s’il existe déjà un abonnement actif
        Subscription sub = subscriptionRepository.findByUserIdAndActiveTrue(user.getId())
                .orElseGet(() -> {
                    Subscription newSub = new Subscription();
                    newSub.setUser(user);
                    newSub.setStartDate(LocalDate.now());
                    return newSub;
                });

        // Met à jour les informations du même abonnement
        sub.setSubscriptionPlan(plan);
        sub.setStartDate(LocalDate.now());
        sub.setEndDate(LocalDate.now().plusMonths(months));
        sub.setActive(true);
        sub.setPaidAmount(totalCost);
        sub.setInstallmentCount(months);
        sub.setRemainingProjects(plan.isUnlimitedProjects() ? -1 : plan.getProjectLimit());
        sub.setStatus(PaymentCallStatusEnum.PAID);
        sub.setRenewed(true);
        sub.setDateInvoice(LocalDate.now());

        Subscription savedSub = subscriptionRepository.save(sub);

        return savedSub;
    }

    @Override
    public Subscription renewSubscription(Long subscriptionId, int months) {
        Subscription old = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        old.setRenewed(true);
        old.setEndDate(LocalDate.now().plusMonths(months));
        return subscriptionRepository.save(old);
    }

    @Override
    public boolean isSubscriptionActive(Long userId) {
        return subscriptionRepository.existsByUserIdAndActiveTrue(userId);
    }

    @Override
    public boolean canCreateProject(Long userId) {
        Subscription sub = subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Active subscription required"));
        return sub.canCreateNewProject();
    }

    @Override
    public void onProjectCreated(Long userId) {
        Subscription sub = subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Active subscription required"));
        sub.incrementProjects();
        subscriptionRepository.save(sub);
    }

    @Override
    public void onProjectDeleted(Long userId) {
        Subscription sub = subscriptionRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Active subscription required"));
        sub.decrementProjects();
        subscriptionRepository.save(sub);
    }

    @Override
    public Subscription payInstallment(Long subscriptionId, double amount) {
        Subscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        sub.setPaidAmount(sub.getPaidAmount() + amount);
        if (sub.getPaidAmount() >= sub.getSubscriptionPlan().getTotalCost()) {
            sub.setStatus(PaymentCallStatusEnum.PAID);
        }
        return subscriptionRepository.save(sub);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Page<Subscription> getUserSubscriptionsOrderedByEndDate(Long userId, Pageable pageable) {
        return subscriptionRepository.findByUserIdOrderByEndDateDesc(userId, pageable);
    }

    @Override
    public SubscriptionKpiDto getSubscriptionKpis() {
        List<Subscription> allSubs = subscriptionRepository.findAll();
        List<Invoice> allInvoices = invoiceRepository.findAll();

        long totalSubs = allSubs.size();
        long activeSubs = allSubs.stream().filter(Subscription::isActive).count();
        long expiredSubs = totalSubs - activeSubs;

        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minus(7, ChronoUnit.DAYS);
        LocalDate monthAgo = today.minus(30, ChronoUnit.DAYS);
        LocalDate yearAgo = today.minus(365, ChronoUnit.DAYS);

        long weeklySubs = allSubs.stream().filter(s -> s.getStartDate().isAfter(weekAgo)).count();
        long monthlySubs = allSubs.stream().filter(s -> s.getStartDate().isAfter(monthAgo)).count();
        long yearlySubs = allSubs.stream().filter(s -> s.getStartDate().isAfter(yearAgo)).count();

        double totalRevenue = allInvoices.stream().mapToDouble(Invoice::getAmount).sum();
        double paidRevenue = allInvoices.stream().filter(Invoice::isPaid).mapToDouble(Invoice::getAmount).sum();
        double unpaidRevenue = totalRevenue - paidRevenue;

        double weeklyRevenue = allInvoices.stream()
                .filter(inv -> inv.getCreatedAt().isAfter(LocalDateTime.now().minusWeeks(1)))
                .mapToDouble(Invoice::getAmount).sum();

        double monthlyRevenue = allInvoices.stream()
                .filter(inv -> inv.getCreatedAt().isAfter(LocalDateTime.now().minusMonths(1)))
                .mapToDouble(Invoice::getAmount).sum();

        double yearlyRevenue = allInvoices.stream()
                .filter(inv -> inv.getCreatedAt().isAfter(LocalDateTime.now().minusYears(1)))
                .mapToDouble(Invoice::getAmount).sum();

        double paidPercentage = totalRevenue > 0 ? (paidRevenue / totalRevenue) * 100 : 0;
        double unpaidPercentage = 100 - paidPercentage;

        return new SubscriptionKpiDto(
                totalSubs,
                activeSubs,
                expiredSubs,
                weeklySubs,
                monthlySubs,
                yearlySubs,
                totalRevenue,
                weeklyRevenue,
                monthlyRevenue,
                yearlyRevenue,
                paidRevenue,
                unpaidRevenue,
                paidPercentage,
                unpaidPercentage);
    }

    @Override
    public Subscription getSubscriptionByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Aucun abonnement trouvé pour cet utilisateur."));
    }

    public List<MonthlyStatsDTO> getSubscriptionEvolution(Integer year) {

        int targetYear = (year != null) ? year : LocalDate.now().getYear();

        // Récupère les données réelles
        List<Object[]> results = subscriptionRepository.countSubscriptionsByMonth(targetYear);

        // Map mois -> nombre trouvé
        Map<Integer, Long> data = new HashMap<>();
        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            Long total = (Long) row[1];
            data.put(month, total);
        }

        // Liste finale avec les 12 mois (même si = 0)
        List<MonthlyStatsDTO> stats = new ArrayList<>();

        String[] monthNames = {
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };

        for (int i = 1; i <= 12; i++) {
            stats.add(new MonthlyStatsDTO(
                    monthNames[i - 1],
                    data.getOrDefault(i, 0L)));
        }

        return stats;
    }

    public List<SubscriptionPlanPercentageDTO> getSubscriptionPlanDistribution() {

        long total = subscriptionRepository.countAllSubscriptions();

        // Récupérer les comptes existants
        List<Object[]> rawData = subscriptionRepository.countSubscriptionsByPlan();

        // Transformer en map pour accès rapide
        Map<SubscriptionPlanEnum, Long> countMap = new HashMap<>();
        for (Object[] row : rawData) {
            SubscriptionPlanEnum plan = (SubscriptionPlanEnum) row[0];
            Long count = (Long) row[1];
            countMap.put(plan, count);
        }

        // Retourner tous les plans sauf WORKER
        List<SubscriptionPlanPercentageDTO> result = new ArrayList<>();

        for (SubscriptionPlanEnum planEnum : SubscriptionPlanEnum.values()) {

            if (planEnum == SubscriptionPlanEnum.WORKER)
                continue; // exclure WORKER

            long count = countMap.getOrDefault(planEnum, 0L);

            double percentage = (total == 0) ? 0.0 : (count * 100.0) / total;

            result.add(
                    new SubscriptionPlanPercentageDTO(
                            planEnum.name(),
                            Math.round(percentage * 100.0) / 100.0));
        }

        return result;
    }

}
