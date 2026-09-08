package com.wakana.realestateworks.controller;

import com.wakana.realestateworks.dto.CriticalTaskDto;
import com.wakana.realestateworks.dto.TaskRequest;
import com.wakana.realestateworks.dto.response.ExecutorDashboardResponse;
import com.wakana.realestateworks.dto.response.TaskResponseDto;
import com.wakana.realestateworks.dto.response.TaskStatusKpiResponse;
import com.wakana.realestateworks.enums.TaskStatusEnum;
import com.wakana.realestateworks.model.Task;
import com.wakana.realestateworks.model.TaskDocument;
import com.wakana.realestateworks.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TaskResponseDto createTask(@ModelAttribute TaskRequest request) {
        return taskService.createTask(request).convertToDto();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TaskResponseDto  updateTask(@PathVariable Long id, @ModelAttribute TaskRequest request) {
        return taskService.updateTask(id, request).convertToDto();
    }

    @GetMapping("/{id}")
    public TaskResponseDto  getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id).convertToDto();
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}/status")
    public TaskResponseDto updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatusEnum status) {
        return taskService.updateTaskStatus(id, status.name()).convertToDto();
    }

    /*
     * @GetMapping
     * public Page<Task> getAllTasks(@RequestParam(defaultValue = "0") int page,
     * 
     * @RequestParam(defaultValue = "10") int size) {
     * Pageable pageable = PageRequest.of(page, size);
     * return taskService.getAllTasks(pageable);
     * }
     */

    @GetMapping("/by-property/{propertyId}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByProperty(@PathVariable Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Task> taskPage = taskService.getTasksByProperty(propertyId, pageable);

        Page<TaskResponseDto> responsePage = taskPage.map(Task::convertToDto);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/critical")
    public List<CriticalTaskDto> getCriticalTasks(
            @RequestParam Long promoterId,
            @RequestParam(required = false) Long realEstatePropertyId,
            @RequestParam(required = false) Integer year) {
        return taskService.getCriticalTasks(promoterId, realEstatePropertyId, year);
    }

    @GetMapping("/kpis")
    public Object getTaskKpis(
            @RequestParam Long promoterId,
            @RequestParam(required = false) Long realEstatePropertyId,
            @RequestParam(required = false) Integer year) {
        return taskService.getTaskKpis(promoterId, realEstatePropertyId, year);
    }

    @GetMapping("/by-executor/{executorId}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByExecutor(
            @PathVariable Long executorId,
            @RequestParam(required = false) TaskStatusEnum status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskService.getTasksByExecutor(executorId, status, pageable);

        Page<TaskResponseDto> responsePage = taskPage.map(Task::convertToDto);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/late/count")
    public Long getLateTasksCount(@RequestParam Long promoterId) {
        return taskService.countLateTasks(promoterId);
    }

    @PostMapping(value = "/{taskId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TaskDocument> addDocumentToTask(
            @PathVariable Long taskId,
            @RequestParam("libelle") String libelle,
            @RequestParam("file") MultipartFile file) {

        TaskDocument savedDoc = taskService.addDocumentToTask(taskId, libelle, file);
        return ResponseEntity.ok(savedDoc);
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocumentFromTask(@PathVariable Long documentId) {
        taskService.removeDocumentFromTask(documentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard/mobile/{executorId}")
    public ResponseEntity<ExecutorDashboardResponse> getExecutorDashboard(@PathVariable Long executorId) {
        ExecutorDashboardResponse response = taskService.getExecutorDashboard(executorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status-distribution/{executorId}")
    public ResponseEntity<List<TaskStatusKpiResponse>> getTaskStatusDistributionByExecutor(
            @PathVariable Long executorId) {

        List<TaskStatusKpiResponse> kpi = taskService.getTaskStatusDistribution(executorId);
        return ResponseEntity.ok(kpi);
    }

}
