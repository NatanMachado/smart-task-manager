package com.codeuai.smarttaskmanager.dto;

public record DashboardSummary(
        long totalTasks,
        long completedTasks,
        long pendingTasks
) {}