package org.example.schedule.controller.response.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GetSchedulesDto(
        List<ScheduleDto> schedules,
        int totalPages,
        long totalElements
) {}