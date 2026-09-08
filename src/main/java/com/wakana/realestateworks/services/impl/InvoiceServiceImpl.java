package com.wakana.realestateworks.services.impl;

import com.wakana.realestateworks.dto.response.InvoiceResponseDto;
import com.wakana.realestateworks.dto.response.MonthlyRevenueDTO;
import com.wakana.realestateworks.model.Invoice;
import com.wakana.realestateworks.model.Subscription;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.repository.InvoiceRepository;
import com.wakana.realestateworks.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice generateInvoice(User user, Subscription subscription, double amount, String paymentMethod) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setUser(user);
        invoice.setSubscription(subscription);
        invoice.setAmount(amount);
        invoice.setPaid(true); // car la souscription est payée
        invoice.setPaymentMethod(paymentMethod);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Page<Invoice> getInvoicesByUser(Long userId, Pageable pageable) {
        return invoiceRepository.findByUserId(userId, pageable);
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenueEvolution(Integer year) {

        int targetYear = (year != null) ? year : LocalDate.now().getYear();

        List<Object[]> results = invoiceRepository.getMonthlyRevenues(targetYear);

        Map<Integer, Double> map = new HashMap<>();
        for (Object[] row : results) {
            Integer month = (Integer) row[0];
            Double total = (Double) row[1];
            map.put(month, total);
        }

        String[] months = {
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };

        List<MonthlyRevenueDTO> list = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            list.add(new MonthlyRevenueDTO(
                    months[i - 1],
                    map.getOrDefault(i, 0.0)));
        }

        return list;
    }

    @Override
    public Page<InvoiceResponseDto> getInvoices(Pageable pageable) {
        return invoiceRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(InvoiceResponseDto::fromEntity); // utilise ton DTO avec formatage et labels
    }
}
