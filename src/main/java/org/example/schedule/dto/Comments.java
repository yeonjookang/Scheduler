package org.example.schedule.dto;

import java.time.LocalDateTime;

public record Comments (
        String content,
        String name,
        LocalDateTime createAt,
        LocalDateTime modifyAt
) {
}
