package com.wakana.realestateworks.dto;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data

public class StockDashboardKpiDto {
    private long totalOrders;
    private long pendingOrders;
    private long deliveredOrders;
    private long totalStockItems;
    private long criticalStockCount;
    private double totalStockQuantity;
    private long totalQuoteCount;
    private double totalQuoteValue;
}
