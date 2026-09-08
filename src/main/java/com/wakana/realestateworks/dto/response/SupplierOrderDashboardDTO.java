package com.wakana.realestateworks.dto.response;




import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SupplierOrderDashboardDTO {
    private long totalOrders;
    private List<StatusCount> statusCounts;
    private List<StatusPercent> statusPercents;
    private List<TopProperty> topProperties;

    @Data @AllArgsConstructor
    public static class StatusCount {
        private String status;
        private long count;
    }

    @Data @AllArgsConstructor
    public static class StatusPercent {
        private String status;
        private double percent;
    }

    @Data @AllArgsConstructor
    public static class TopProperty {
        private Long propertyId;
        private String propertyLabel;
        private long orderCount;
    }
}
