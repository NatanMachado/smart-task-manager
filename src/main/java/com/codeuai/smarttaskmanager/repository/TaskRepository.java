package com.codeuai.smarttaskmanager.repository;

import com.codeuai.smarttaskmanager.model.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserUsername(String username);

    Page<Task> findByUserUsername(String username, Pageable pageable);

    @Query("""
            SELECT t FROM Task t
            WHERE t.user.username = :username
            AND (:completed IS NULL OR t.completed = :completed)
            AND (:query IS NULL OR
            LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) OR
            LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')))
            """)
    Page<Task> search(String username, Boolean completed, String query, Pageable pageable);

    long countByUserUsername(String username);

    long countByUserUsernameAndCompleted(String username, boolean completed);

    List<Task> findByCreatedAtBeforeAndArchivedFalse(LocalDateTime cutoffDate);

    List<Task> findByCreatedAtBeforeAndCompletedFalse(LocalDateTime cutoffDate);

    // Custom query to find tasks due soon (e.g., within the next 3 days)
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :now AND :soon")
    List<Task> findTasksDueSoon(@Param("now") LocalDateTime now, @Param("soon") LocalDateTime soon);
}