package org.example.schedule.controller.response.dto;

import lombok.Builder;

@Builder
public record ScheduleDto(
        Long scheduleId,
        String name,
        String title
) {}
