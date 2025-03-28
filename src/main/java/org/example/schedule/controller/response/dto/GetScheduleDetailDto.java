package org.example.schedule.controller.response.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetScheduleDetailDto(
        String name,
        String email,
        String title,
        String content,
        LocalDateTime createAt,
        LocalDateTime modifyAt
) {}
