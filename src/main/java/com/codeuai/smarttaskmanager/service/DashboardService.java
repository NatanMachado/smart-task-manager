package com.codeuai.smarttaskmanager.service;

import com.codeuai.smarttaskmanager.dto.DashboardSummary;
import com.codeuai.smarttaskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;

    private String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Cacheable(value = "dashboard", key = "#root.methodName + '_' + #root.target.getCurrentUsername()")
    public DashboardSummary getSummary() {
        String username = getCurrentUsername();

        long total = taskRepository.countByUserUsername(username);
        long completed = taskRepository.countByUserUsernameAndCompleted(username, true);
        long pending = total - completed;

        return new DashboardSummary(total, completed, pending);
    }
}