package com.wakana.realestateworks.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wakana.realestateworks.dto.response.RealEstateResponseDto;
import com.wakana.realestateworks.dto.response.TaskResponseDto;
import com.wakana.realestateworks.dto.response.UserResponseDto;
import com.wakana.realestateworks.enums.TaskPriorityEnum;
import com.wakana.realestateworks.enums.TaskStatusEnum;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskPriorityEnum priority;

    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_estate_property_id")
    private RealEstateProperty realEstateProperty;

    @ManyToMany
    @JoinTable(name = "task_executors", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> executors;

    @ElementCollection
    private List<String> pictures;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
    
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TaskDocument> documents;

    public TaskResponseDto convertToDto() {
        RealEstateResponseDto realEstateResponseDto = null;
        if (this.realEstateProperty != null) {
            realEstateResponseDto = this.realEstateProperty.convertToRealEstateDto();
        }

        return new TaskResponseDto(
                this.getId(),
                this.getTitle(),
                this.getDescription(),
                this.getPriority(),
                this.getStatus(),
                realEstateResponseDto,
                this.getExecutors().stream()
                        .map(user -> new UserResponseDto(user.getId(), user.getPrenom(), user.getNom(),
                                user.getTelephone(),user.getPhoto(),user.getProfil()))
                        .toList(),
                this.getPictures(),
                this.getStartDate(),

                this.getEndDate(),
                this.getDocuments()

        );
    }

}
