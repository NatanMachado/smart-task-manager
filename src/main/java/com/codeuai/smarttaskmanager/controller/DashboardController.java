package com.codeuai.smarttaskmanager.controller;

import com.codeuai.smarttaskmanager.dto.DashboardSummary;
import com.codeuai.smarttaskmanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardSummary getSummary() {
        return dashboardService.getSummary();
    }
}