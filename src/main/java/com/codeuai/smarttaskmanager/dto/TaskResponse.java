package com.codeuai.smarttaskmanager.dto;

public record TaskResponse(
        Long id,
        String title,
        String description,
        boolean completed
) {}