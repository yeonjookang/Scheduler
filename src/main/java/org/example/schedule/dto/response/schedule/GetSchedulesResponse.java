package org.example.schedule.dto.response.schedule;

import lombok.Builder;
import org.example.schedule.dto.ScheduleSummary;

import java.util.List;

@Builder
public record GetSchedulesResponse(
        List<ScheduleSummary> schedules,
        int totalPages,
        long totalElements
) {}