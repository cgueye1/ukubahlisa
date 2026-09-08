package com.wakana.realestateworks.services;

import com.wakana.realestateworks.dto.CriticalTaskDto;
import com.wakana.realestateworks.dto.TaskRequest;
import com.wakana.realestateworks.dto.response.ExecutorDashboardResponse;
import com.wakana.realestateworks.dto.response.TaskStatusKpiResponse;
import com.wakana.realestateworks.enums.TaskStatusEnum;
import com.wakana.realestateworks.model.Task;
import com.wakana.realestateworks.model.TaskDocument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskService {

    Task createTask(TaskRequest request);

    Task updateTask(Long id, TaskRequest request);

    void deleteTask(Long id);

    Task getTaskById(Long id);

    Task updateTaskStatus(Long id, String status);

    List<CriticalTaskDto> getCriticalTasks(Long promoterId, Long realEstatePropertyId, Integer year);

    Page<Task> getTasksByProperty(Long propertyId, Pageable pageable);

    Object getTaskKpis(Long promoterId, Long realEstatePropertyId, Integer year); // You can create a specific DTO if
                                                                                  // needed

    Page<Task> getAllTasks(Pageable pageable);

    Page<Task> getTasksByExecutor(Long executorId, TaskStatusEnum status, Pageable pageable);

    Long countLateTasks(Long promoterId);

    TaskDocument addDocumentToTask(Long taskId, String libelle, MultipartFile file);

    void removeDocumentFromTask(Long documentId);

    ExecutorDashboardResponse getExecutorDashboard(Long executorId);
    
    
    List<TaskStatusKpiResponse> getTaskStatusDistribution(Long executorId);

}
