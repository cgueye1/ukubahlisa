package com.wakana.realestateworks.repository;

import com.wakana.realestateworks.model.Invoice;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT MONTH(i.createdAt) AS month, SUM(i.amount) AS total " +
            "FROM Invoice i " +
            "WHERE YEAR(i.createdAt) = :year AND i.paid = true " +
            "GROUP BY MONTH(i.createdAt)")
    List<Object[]> getMonthlyRevenues(@Param("year") int year);
    
    
      Page<Invoice> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
