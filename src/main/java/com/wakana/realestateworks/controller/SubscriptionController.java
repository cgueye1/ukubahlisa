package com.wakana.realestateworks.controller;

import com.wakana.realestateworks.dto.response.InvoiceResponseDto;
import com.wakana.realestateworks.dto.response.MonthlyRevenueDTO;
import com.wakana.realestateworks.dto.response.MonthlyStatsDTO;
import com.wakana.realestateworks.dto.response.SubscriptionKpiDto;
import com.wakana.realestateworks.dto.response.SubscriptionPlanPercentageDTO;
import com.wakana.realestateworks.model.Subscription;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.services.InvoiceService;
import com.wakana.realestateworks.services.SubscriptionService;
import com.wakana.realestateworks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;
    private final InvoiceService invoiceService;

    /**
     * ✅ Créer un abonnement
     */
    @GetMapping("/create/{userId}/{planId}/{months}")
    public ResponseEntity<?> createSubscription(
            @PathVariable Long userId,
            @PathVariable Long planId,
            @PathVariable int months,
            @RequestParam(required = false) String num_transaction_from_gu,
            @RequestParam(required = false) String num_command,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) String errorCode) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        boolean paymentOk = "200".equals(errorCode.toString());

        if (paymentOk) {
            Subscription subscription = subscriptionService.createSubscription(user, planId, months);
            if (subscription != null) {
                invoiceService.generateInvoice(user, subscription, subscription.getPaidAmount(), "");

            }

            return ResponseEntity.ok("Paiement éffectué ");

        } else {
            return ResponseEntity.ok("Erreur de paiement");
        }

    }

    /**
     * 🔄 Renouveler un abonnement
     */
    /*
     * @PutMapping("/{subscriptionId}/renew")
     * public ResponseEntity<Subscription> renewSubscription(
     * 
     * @PathVariable Long subscriptionId,
     * 
     * @RequestParam(defaultValue = "1") int months) {
     * return
     * ResponseEntity.ok(subscriptionService.renewSubscription(subscriptionId,
     * months));
     * }
     */

    /**
     * 💳 Payer une échéance (paiement partiel)
     */
    /*
     * @PutMapping("/{subscriptionId}/pay")
     * public ResponseEntity<Subscription> payInstallment(
     * 
     * @PathVariable Long subscriptionId,
     * 
     * @RequestParam double amount) {
     * return ResponseEntity.ok(subscriptionService.payInstallment(subscriptionId,
     * amount));
     * }
     */

    /**
     * ✅ Vérifier si l’abonnement est actif pour un utilisateur
     */
    @GetMapping("/is-active/{userId}")
    public ResponseEntity<Boolean> isSubscriptionActive(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.isSubscriptionActive(userId));
    }

    /**
     * 🏗️ Vérifier si l’utilisateur peut créer un projet
     */
    @GetMapping("/can-create-project/{userId}")
    public ResponseEntity<Boolean> canCreateProject(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.canCreateProject(userId));
    }

    /**
     * 🗂️ Lister tous les abonnements
     */
    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    /**
     * 📅 Lister les abonnements d’un utilisateur (triés par date de fin)
     */
    @GetMapping("/user/{userId}")
    public Subscription getUserSubscription(@PathVariable Long userId) {
        return subscriptionService.getSubscriptionByUserId(userId);
    }
    /*
     * * @GetMapping("/user/{userId}")
     * public ResponseEntity<Page<Subscription>> getUserSubscriptions(
     * 
     * @PathVariable Long userId,
     * Pageable pageable) {
     * return
     * ResponseEntity.ok(subscriptionService.getUserSubscriptionsOrderedByEndDate(
     * userId, pageable));
     * }
     */

    @GetMapping("/invoices/{userId}")
    public ResponseEntity<Page<InvoiceResponseDto>> getUserInvoices(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<InvoiceResponseDto> invoices = invoiceService
                .getInvoicesByUser(userId, PageRequest.of(page, size))
                .map(InvoiceResponseDto::fromEntity);

        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/dashbord")
    public SubscriptionKpiDto getSubscriptionKpis() {
        return subscriptionService.getSubscriptionKpis();
    }

    @GetMapping("/evolution")
    public List<MonthlyStatsDTO> getEvolution(@RequestParam(required = false) Integer year) {
        return subscriptionService.getSubscriptionEvolution(year);
    }

    @GetMapping("/revenues/evolution")
    public List<MonthlyRevenueDTO> getRevenueEvolution(
            @RequestParam(required = false) Integer year) {
        return invoiceService.getMonthlyRevenueEvolution(year);
    }

    @GetMapping("/plan-distribution")
    public ResponseEntity<List<SubscriptionPlanPercentageDTO>> getSubscriptionPlanDistribution() {
        return ResponseEntity.ok(subscriptionService.getSubscriptionPlanDistribution());
    }
    
    
     @GetMapping("/invoices-lastest")
    public Page<InvoiceResponseDto> getInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return invoiceService.getInvoices(pageable);
    }
}
