package com.codeuai.smarttaskmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.codeuai.smarttaskmanager.model.Task;
import com.codeuai.smarttaskmanager.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskCleanupService {
    private final TaskRepository taskRepository;

    @Value("${tasks.archiveDays:30}")
    private int archiveDays;

    @Value("${tasks.reminderDays:3}")
    private int reminderDays;

    @Value("${tasks.cleanupDays:90}")
    private int cleanupDays;

    // Auto-archive tasks older than X days
    @Scheduled(cron = "0 0 2 * * ?") // Runs daily at 2 AM. https://crontab.guru/
    public void autoArchiveOldTasks() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(archiveDays);
        List<Task> oldTasks = taskRepository.findByCreatedAtBeforeAndArchivedFalse(cutoffDate);

        for (Task task : oldTasks) {
            task.setArchived(true);
            taskRepository.save(task);
        }
    }

    // Send reminders for tasks
    @Scheduled(cron = "0 0 9 * * ?") // Runs daily at 9 AM. https://crontab.guru/
    public void sendTaskReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusDays(reminderDays); // within the next N days
        List<Task> tasks = taskRepository.findTasksDueSoon(now, soon);

        for (Task task : tasks) {
            // Logic to send reminders (e.g., email, notification)
            System.out.println("Reminder sent for task: " + task.getId());
        }
    }

    // Clean up stale tasks
    @Scheduled(cron = "0 0 1 * * ?") // Runs daily at 1 AM. https://crontab.guru/
    public void cleanUpStaleTasks() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(cleanupDays);
        List<Task> staleTasks = taskRepository.findByCreatedAtBeforeAndCompletedFalse(cutoffDate);

        taskRepository.deleteAll(staleTasks);
    }
}
