
package com.wakana.realestateworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wakana.realestateworks.model.RealEstateProperty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RealEstatePropertyRepository
                extends JpaRepository<RealEstateProperty, Long>, JpaSpecificationExecutor<RealEstateProperty> {

        // Liste - Promoteur/MOA/Manager

        @Query("""
                            SELECT p FROM RealEstateProperty p
                            WHERE
                                (p.promoter.id = :promoterId
                                 OR p.moa.id = :promoterId
                                 OR p.manager.id = :promoterId)
                                AND p.parentProperty IS NULL
                            
                                AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
                            ORDER BY p.createdAt DESC
                        """)
        Page<RealEstateProperty> searchByPromoterAndName(
                        @Param("promoterId") Long promoterId,
                        @Param("name") String name,
                        Pageable pageable);



}
