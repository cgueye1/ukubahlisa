package com.wakana.realestateworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wakana.realestateworks.model.TaskDocument;

public interface TaskDocumentRepository extends JpaRepository<TaskDocument, Long> {
}
