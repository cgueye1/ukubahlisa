package com.wakana.realestateworks.dto;

import lombok.Data;
import java.util.List;

@Data
public class SupplierQuoteRequest {
    private Long orderId;
    private List<QuoteItemRequest> items;
    private Long generedById;
}
