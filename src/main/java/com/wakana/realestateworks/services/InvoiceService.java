package com.wakana.realestateworks.services;

import com.wakana.realestateworks.dto.response.InvoiceResponseDto;
import com.wakana.realestateworks.dto.response.MonthlyRevenueDTO;
import com.wakana.realestateworks.model.Invoice;
import com.wakana.realestateworks.model.Subscription;
import com.wakana.realestateworks.model.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceService {
    Invoice generateInvoice(User user, Subscription subscription, double amount, String paymentMethod);

    Page<Invoice> getInvoicesByUser(Long userId, Pageable pageable);

    List<MonthlyRevenueDTO> getMonthlyRevenueEvolution(Integer year);

    Page<InvoiceResponseDto> getInvoices(Pageable pageable);
}
