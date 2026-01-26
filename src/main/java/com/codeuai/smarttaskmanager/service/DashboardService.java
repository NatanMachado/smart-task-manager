package com.codeuai.smarttaskmanager.service;

import com.codeuai.smarttaskmanager.dto.DashboardSummary;
import com.codeuai.smarttaskmanager.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService extends SessionService {

    private final TaskRepository taskRepository;

    @Cacheable(value = "dashboard:getSummary", keyGenerator = "userKeyGenerator")
    public DashboardSummary getSummary() {
        String username = super.getCurrentUsername();

        long total = taskRepository.countByUserUsername(username);
        long completed = taskRepository.countByUserUsernameAndCompleted(username, true);
        long pending = total - completed;

        return new DashboardSummary(total, completed, pending);
    }
}