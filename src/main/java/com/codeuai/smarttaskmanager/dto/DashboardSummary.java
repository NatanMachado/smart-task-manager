package com.codeuai.smarttaskmanager.dto;

import java.io.Serializable;

public record DashboardSummary(long total, long completed, long pending) implements Serializable {

    private static final long serialVersionUID = 1L;
}
