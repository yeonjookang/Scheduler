package org.example.schedule.dto.response.schedule;

import java.time.LocalDateTime;

public record GetScheduleResponse(
        String name,
        String email,
        String title,
        String content,
        LocalDateTime createAt,
        LocalDateTime modifyAt
) {}
