package com.codeuai.smarttaskmanager.dto;

public record TaskRequest(
        String title,
        String description,
        boolean completed
) {}
