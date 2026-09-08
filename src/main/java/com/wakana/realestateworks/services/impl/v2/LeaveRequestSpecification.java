package com.wakana.realestateworks.services.impl.v2;


import com.wakana.realestateworks.enums.LeaveRequestStatus;
import com.wakana.realestateworks.model.v2.LeaveRequest;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class LeaveRequestSpecification {

    private LeaveRequestSpecification() {}

    /** Filter by real estate property */
    public static Specification<LeaveRequest> byRealEstate(Long realEstateId) {
        return (root, query, cb) ->
                cb.equal(root.get("realEstateProperty").get("id"), realEstateId);
    }

    /** Filter by status — ignored if status is null */
    public static Specification<LeaveRequest> byStatus(LeaveRequestStatus status) {
        return (root, query, cb) ->
                status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    /**
     * Search by worker firstName, lastName, or "firstName lastName".
     * Case-insensitive, partial match. Ignored if search is null/blank.
     */
    public static Specification<LeaveRequest> byWorkerSearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return cb.conjunction();

            String pattern = "%" + search.trim().toLowerCase() + "%";
            Join<Object, Object> worker = root.join("worker", JoinType.INNER);

            Expression<String> firstName = cb.lower(worker.get("firstName"));
            Expression<String> lastName  = cb.lower(worker.get("lastName"));
            Expression<String> fullName  = cb.lower(
                    cb.concat(cb.concat(worker.get("firstName"), " "), worker.get("lastName")));

            return cb.or(
                    cb.like(firstName, pattern),
                    cb.like(lastName,  pattern),
                    cb.like(fullName,  pattern)
            );
        };
    }
}