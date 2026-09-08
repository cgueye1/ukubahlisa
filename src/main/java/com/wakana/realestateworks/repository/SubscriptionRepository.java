package com.wakana.realestateworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wakana.realestateworks.enums.PaymentCallStatusEnum;
import com.wakana.realestateworks.model.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserId(Long userId);

    List<Subscription> findByActiveTrueAndStatus(PaymentCallStatusEnum status);

    Page<Subscription> findByUserIdOrderByEndDateDesc(Long userId, Pageable pageable);

    Page<Subscription> findByUserIdAndStatusOrderByEndDateDesc(Long userId, PaymentCallStatusEnum status,
            Pageable pageable);

    boolean existsByUserIdAndActiveTrue(Long userId);

    Optional<Subscription> findByUserIdAndActiveTrue(Long userId);

    @Query("SELECT MONTH(s.startDate) AS month, COUNT(s) AS total " +
            "FROM Subscription s " +
            "WHERE YEAR(s.startDate) = :year " +
            "GROUP BY MONTH(s.startDate)")
    List<Object[]> countSubscriptionsByMonth(@Param("year") int year);

  

    @Query("SELECT s.subscriptionPlan.name, COUNT(s) FROM Subscription s GROUP BY s.subscriptionPlan.name")
    List<Object[]> countSubscriptionsByPlan();

    @Query("SELECT COUNT(s) FROM Subscription s")
    long countAllSubscriptions();
    /// Page<Subscription> findByUserIdOrderByEndDateDesc(Long userId, Pageable
    /// pageable);

}