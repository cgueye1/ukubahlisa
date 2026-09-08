package com.wakana.realestateworks.repository;

import com.wakana.realestateworks.enums.TaskStatusEnum;
import com.wakana.realestateworks.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByPriorityOrderByStartDateAsc(String priority);

    List<Task> findByRealEstatePropertyId(Long propertyId);

    Page<Task> findByRealEstatePropertyId(Long propertyId, Pageable pageable);

    Page<Task> findByExecutorsId(Long executorId, Pageable pageable);

    Page<Task> findByExecutorsIdAndStatus(Long executorId, TaskStatusEnum status, Pageable pageable);

    @Query("""
                SELECT t FROM Task t
                WHERE t.realEstateProperty.promoter.id = :promoterId
                   OR t.realEstateProperty.moa.id = :promoterId
                   OR t.realEstateProperty.manager.id = :promoterId
            """)
    List<Task> findByRealEstateProperty_Promoter_Id(@Param("promoterId") Long promoterId);

    @Query("""
                SELECT COUNT(t) FROM Task t
                WHERE t.status <> com.wakana.realestateworks.enums.TaskStatusEnum.DONE
                  AND t.endDate IS NOT NULL
                  AND t.endDate < CURRENT_TIMESTAMP
                  AND (
                       t.realEstateProperty.promoter.id = :promoterId
                       OR t.realEstateProperty.moa.id = :promoterId
                       OR t.realEstateProperty.manager.id = :promoterId
                  )
            """)
    Long countLateTasksByPromoter(@Param("promoterId") Long promoterId);

    List<Task> findAllByExecutors_Id(Long executorId);

    @Query("SELECT t.status, COUNT(t) FROM Task t JOIN t.executors e WHERE e.id = :executorId GROUP BY t.status")
    List<Object[]> countTasksByStatusForExecutor(@Param("executorId") Long executorId);

}
