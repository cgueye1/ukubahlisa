package com.wakana.realestateworks.services.impl;

import com.wakana.realestateworks.dto.CriticalTaskDto;
import com.wakana.realestateworks.dto.TaskRequest;
import com.wakana.realestateworks.dto.response.ExecutorDashboardResponse;
import com.wakana.realestateworks.dto.response.TaskStatusKpiResponse;
import com.wakana.realestateworks.enums.TaskStatusEnum;
import com.wakana.realestateworks.enums.TaskPriorityEnum;
import com.wakana.realestateworks.model.RealEstateProperty;
import com.wakana.realestateworks.model.Task;
import com.wakana.realestateworks.model.TaskDocument;
import com.wakana.realestateworks.model.User;
import com.wakana.realestateworks.repository.RealEstatePropertyRepository;
import com.wakana.realestateworks.repository.TaskDocumentRepository;
import com.wakana.realestateworks.repository.TaskRepository;
import com.wakana.realestateworks.repository.UserRepository;
import com.wakana.realestateworks.services.TaskService;
import com.wakana.realestateworks.util.FileTransferUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final RealEstatePropertyRepository realEstatePropertyRepository;
    private final TaskDocumentRepository taskDocumentRepository;

    @Override
    @Transactional
    public Task createTask(TaskRequest request) {
        Task task = new Task();
        return saveOrUpdateTask(task, request);
    }

    @Override
    @Transactional
    public Task updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        return saveOrUpdateTask(task, request);
    }

    private Task saveOrUpdateTask(Task task, TaskRequest request) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        task.setPriority(TaskPriorityEnum.valueOf(request.getPriority()));
        task.setStatus(TaskStatusEnum.valueOf(request.getStatus()));

        if (request.getStartDate() != null) {
            task.setStartDate(parseDate(request.getStartDate()).atStartOfDay());
        }

        if (request.getEndDate() != null) {
            task.setEndDate(parseDate(request.getEndDate()).atTime(LocalTime.MAX));
        }

        if (request.getRealEstatePropertyId() != null) {
            RealEstateProperty property = realEstatePropertyRepository.findById(request.getRealEstatePropertyId())
                    .orElseThrow(() -> new RuntimeException("Property not found"));
            task.setRealEstateProperty(property);
        }

        if (request.getExecutorIds() != null && !request.getExecutorIds().isEmpty()) {
            List<User> executors = userRepository.findAllById(request.getExecutorIds());
            task.setExecutors(executors);
        }

        if (request.getPictures() != null && !request.getPictures().isEmpty()) {
            List<String> pictureUrls;
            try {
                pictureUrls = FileTransferUtil.uploadPictures(request.getPictures());
                task.setPictures(pictureUrls);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return taskRepository.save(task);
    }

    private LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return date;
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task updateTaskStatus(Long id, String status) {
        Task task = getTaskById(id);
        task.setStatus(TaskStatusEnum.valueOf(status));
        return taskRepository.save(task);
    }

    @Override
    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> getTasksByProperty(Long propertyId, Pageable pageable) {
        return taskRepository.findByRealEstatePropertyId(propertyId, pageable);
    }

    @Override
    public List<CriticalTaskDto> getCriticalTasks(Long promoterId, Long realEstatePropertyId, Integer year) {
        LocalDate today = LocalDate.now();

        return taskRepository.findByRealEstateProperty_Promoter_Id(promoterId).stream()
                .filter(task -> task.getStatus() != TaskStatusEnum.DONE)
                .filter(task -> task.getEndDate() != null)
                .filter(task -> realEstatePropertyId == null || (task.getRealEstateProperty() != null &&
                        task.getRealEstateProperty().getId().equals(realEstatePropertyId)))
                .filter(task -> year == null || (task.getStartDate() != null &&
                        task.getStartDate().getYear() == year))
                .sorted(Comparator.comparing(Task::getEndDate))
                .limit(4)
                .map(task -> {
                    LocalDate endDate = task.getEndDate().toLocalDate();
                    String color;
                    String statusLabel;

                    if (endDate.isBefore(today)) {
                        color = "#FF0000"; // rouge
                        statusLabel = "En retard";
                    } else if (!endDate.isAfter(today.plusDays(2))) {
                        color = "#FFA500"; // orange clair
                        statusLabel = "Urgent";
                    } else {
                        color = "#00AA00"; // vert
                        statusLabel = "À jour";
                    }

                    return new CriticalTaskDto(
                            task.getId(),
                            endDate,
                            task.getTitle(),
                            task.getStatus().name(),
                            task.getPriority().name(),
                            color,
                            statusLabel);
                })
                .toList();
    }

    @Override
    public Object getTaskKpis(Long promoterId, Long realEstatePropertyId, Integer year) {
        List<Task> allTasks = taskRepository.findByRealEstateProperty_Promoter_Id(promoterId).stream()
                .filter(task -> realEstatePropertyId == null ||
                        (task.getRealEstateProperty() != null
                                && task.getRealEstateProperty().getId().equals(realEstatePropertyId)))
                .filter(task -> year == null ||
                        (task.getStartDate() != null && task.getStartDate().getYear() == year))
                .toList();

        long total = allTasks.size();
        long pending = allTasks.stream()
                .filter(task -> task.getStatus() == TaskStatusEnum.TODO)
                .count();
        long completed = allTasks.stream()
                .filter(task -> task.getStatus() == TaskStatusEnum.DONE)
                .count();
        long overdue = allTasks.stream()
                .filter(task -> task.getEndDate() != null)
                .filter(task -> task.getStatus() != TaskStatusEnum.DONE)
                .filter(task -> task.getEndDate().isBefore(LocalDateTime.now()))
                .count();

        return new Object() {
            public long totalTasks = total;
            public long pendingTasks = pending;
            public long completedTasks = completed;
            public long overdueTasks = overdue;
        };
    }

    @Override
    public Page<Task> getTasksByExecutor(Long executorId, TaskStatusEnum status, Pageable pageable) {
        if (status != null) {
            return taskRepository.findByExecutorsIdAndStatus(executorId, status, pageable);
        } else {
            return taskRepository.findByExecutorsId(executorId, pageable);
        }
    }

    @Override
    public Long countLateTasks(Long promoterId) {
        return taskRepository.countLateTasksByPromoter(promoterId);
    }

    public TaskDocument addDocumentToTask(Long taskId, String libelle, MultipartFile file) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        String filePath;
        try {
            filePath = FileTransferUtil.handleFileUpload(file);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier : " + e.getMessage(), e);
        }

        TaskDocument document = new TaskDocument();
        document.setLibelle(libelle);
        document.setFilePath(filePath);
        document.setTask(task);

        return taskDocumentRepository.save(document);
    }

    public void removeDocumentFromTask(Long documentId) {
        TaskDocument doc = taskDocumentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        taskDocumentRepository.delete(doc);
    }

    @Override
    public ExecutorDashboardResponse getExecutorDashboard(Long executorId) {
        List<Task> tasks = taskRepository.findAllByExecutors_Id(executorId);
        LocalDateTime now = LocalDateTime.now();
        int totalTasks = tasks.size();
        int totalDueTasks = 0;
        int completedTasks = (int) tasks.stream()
                .filter(task -> task.getStatus() == TaskStatusEnum.DONE)
                .count();

        int completedDueTasks = 0;

        for (Task task : tasks) {
            if (task.getEndDate() != null && task.getEndDate().isBefore(now)) {
                totalDueTasks++;
                if (task.getStatus() == TaskStatusEnum.DONE) {
                    completedDueTasks++;
                }
            }
        }

        double performance = totalDueTasks > 0
                ? (completedDueTasks * 100.0) / totalDueTasks
                : 100.0; // Si aucune tâche n'était due, on considère 100% par défaut

        return new ExecutorDashboardResponse(
                totalTasks,
                completedTasks,
                Math.round(performance * 100.0) / 100.0);
    }

    public List<TaskStatusKpiResponse> getTaskStatusDistribution(Long executorId) {
        List<Object[]> results = taskRepository.countTasksByStatusForExecutor(executorId);

        // Convertir la liste SQL en map
        Map<com.wakana.realestateworks.enums.TaskStatusEnum, Long> resultMap = results.stream()
                .collect(Collectors.toMap(
                        obj -> (com.wakana.realestateworks.enums.TaskStatusEnum) obj[0],
                        obj -> (Long) obj[1]));

        // Calcul du total des tâches
        long totalTasks = resultMap.values().stream().mapToLong(Long::longValue).sum();

        // Retourner tous les statuts, même ceux avec 0, en pourcentage
        return Arrays.stream(com.wakana.realestateworks.enums.TaskStatusEnum.values())
                .map(status -> {
                    long count = resultMap.getOrDefault(status, 0L);
                    double percentage = (totalTasks > 0) ? (count * 100.0 / totalTasks) : 0.0;
                    return new TaskStatusKpiResponse(status, percentage);
                })
                .collect(Collectors.toList());
    }

}
