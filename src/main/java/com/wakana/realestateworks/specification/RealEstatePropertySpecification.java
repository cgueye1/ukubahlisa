package com.wakana.realestateworks.specification;
import org.springframework.data.jpa.domain.Specification;

import com.wakana.realestateworks.enums.RealEstatePropertyStatusEnum;
import com.wakana.realestateworks.model.RealEstateProperty;

public class RealEstatePropertySpecification {
       // Default radius of 5 kilometers for the search
       private static final double DEFAULT_RADIUS_KM = 5.0;
       private static final double EARTH_RADIUS_KM = 6371; // 

    public static Specification<RealEstateProperty> hasPropertyType(Long propertyTypeId) {
        return (root, query, criteriaBuilder) -> propertyTypeId != null ?
            criteriaBuilder.equal(root.get("propertyType").get("id"), propertyTypeId) : null;
    }

    public static Specification<RealEstateProperty> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return null;
        };
    }

    public static Specification<RealEstateProperty> addressContains(String address) {
        return (root, query, criteriaBuilder) -> address != null && !address.isEmpty() ?
            criteriaBuilder.like(root.get("address"), "%" + address + "%") : null;
    }
    
    
    
       // Method to find properties within the default radius of the provided location
       public static Specification<RealEstateProperty> withinDefaultRadius(String latitude, String longitude) {
        return (root, query, criteriaBuilder) -> {
            if (latitude != null && longitude != null) {
                // Parse the latitude and longitude values
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);

                // Calculate the latitude and longitude tolerance using the default radius
                double latTolerance = DEFAULT_RADIUS_KM / EARTH_RADIUS_KM;
                double lonTolerance = DEFAULT_RADIUS_KM / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(lat)));

                // Calculate the minimum and maximum latitudes and longitudes for the bounding box
                double minLat = lat - latTolerance;
                double maxLat = lat + latTolerance;
                double minLon = lon - lonTolerance;
                double maxLon = lon + lonTolerance;

                // Return the condition to check if a property is within the bounding box
                return criteriaBuilder.and(
                    criteriaBuilder.between(root.get("latitude"), minLat, maxLat),
                    criteriaBuilder.between(root.get("longitude"), minLon, maxLon)
                );
            }
            return null;
        };
    }
       
          // Méthode pour vérifier que parentProperty est différent de null
    public static Specification<RealEstateProperty> hasParentProperty() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("parentProperty"));
    }
    
    
     // Méthode pour vérifier que status est soit null soit 'AVAILABLE'
    public static Specification<RealEstateProperty> hasAvailableStatus() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                criteriaBuilder.isNull(root.get("status")),
                criteriaBuilder.equal(root.get("status"), RealEstatePropertyStatusEnum.AVAILABLE)
            );
        };
    }
    
    
    
    public static Specification<RealEstateProperty> hasNameContaining(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null && !name.isEmpty()) {
                return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), "%" + name + "%"),
                    criteriaBuilder.like(root.get("parentProperty").get("name"), "%" + name + "%")
                );
            }
            return null; // Aucun filtre si name est null ou vide
        };
    }
    
    
    
    public static Specification<RealEstateProperty> isRentalFalse() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("rental"));
    }

    public static Specification<RealEstateProperty> isCoOwnerFalse() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("coOwner"));
    }
    
    
    public static Specification<RealEstateProperty> isRentalTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("rental"));
    }
    
}
